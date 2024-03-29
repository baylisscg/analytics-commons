package au.edu.uq.aurin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class that consumes output of a process. In addition, it filter output
 * of the REG command on Windows to look for InstallPath registry entry which
 * specifies the location of R.
 */
class StreamHog extends Thread {

  private static final Logger LOG = LoggerFactory.getLogger(StreamHog.class);

  InputStream is;
  boolean capture;
  String installPath;

  StreamHog(final InputStream is, final boolean capture) {
    this.is = is;
    this.capture = capture;
    start();
  }

  public String getInstallPath() {
    return installPath;
  }

  @Override
  public void run() {
    try {
      final BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String line = null;
      while ((line = br.readLine()) != null) {
        if (capture) {
          // we are supposed to capture the output from REG command
          final int i = line.indexOf("InstallPath");
          if (i >= 0) {
            String s = line.substring(i + 11).trim();
            final int j = s.indexOf("REG_SZ");
            if (j >= 0) {
              s = s.substring(j + 6).trim();
            }
            installPath = s;
            LOG.trace("R InstallPath = " + s);
          }
        } else {
          LOG.trace("Rserve>" + line);
        }
      }
    } catch (final IOException e) {
      LOG.error("Rserve Error: ", e);
    }
  }
}

/**
 * Simple class that start Rserve locally if it's not running already - see
 * mainly <code>checkLocalRserve</code> method. It spits out quite some
 * debugging outout of the console, so feel free to modify it for your
 * application if desired.
 * <p>
 *
 * <i>Important:</i> All applications should shutdown every Rserve that they started! Never leave Rserve running if you
 * started it after your application quits since it may pose a security risk. Inform the user if you started an Rserve
 * instance.
 */
public class Rserve {

  private static final Logger LOG = LoggerFactory.getLogger(Rserve.class);

  private Rserve() {
  }

  /**
   * shortcut to <code>launchRserve(cmd, "--no-save --slave", "--no-save --slave", false)</code>
   */
  public static boolean launchRserve(final String cmd) {
    return launchRserve(cmd, "-q --no-save --slave --no-restore", "-q --no-save --slave --no-restore", false);
  }

  /**
   * Attempt to start Rserve. Note: parameters are <b>not</b> quoted, so avoid
   * using any quotes in arguments
   *
   * @param cmd
   *          command necessary to start R
   * @param rargs
   *          arguments are are to be passed to R
   * @param rsrvargs
   *          arguments to be passed to Rserve
   * @return <code>true</code> if Rserve is running or was successfully started, <code>false</code> otherwise.
   */
  public static synchronized boolean launchRserve(final String cmd, final String rargs, final String rsrvargs,
      final boolean debug) {
    try {
      Process p;
      boolean isWindows = false;
      final String osname = System.getProperty("os.name");
      if (osname != null && osname.length() >= 7 && osname.substring(0, 7).equals("Windows")) {
        /* Windows startup */
        isWindows = true;
        p = Runtime.getRuntime().exec(
            "\"" + cmd + "\" -e \"library(Rserve);Rserve(" + (debug ? "TRUE" : "FALSE") + ",args='" + rsrvargs
            + "')\" " + rargs);
      } else {
        /* unix startup */
        p = Runtime.getRuntime().exec(
            new String[] {
                "/bin/sh",
                "-c",
                "echo 'library(Rserve);Rserve(" + (debug ? "TRUE" : "FALSE") + ",args=\"" + rsrvargs + "\")'|" + cmd
                + " " + rargs, });
      }
      LOG.trace("waiting for Rserve to start ... (" + p + ")");
      // Fetch the output - some platforms will die if you don't ...
      final StreamHog errorHog = new StreamHog(p.getErrorStream(), false);
      final StreamHog outputHog = new StreamHog(p.getInputStream(), false);
      if (!isWindows) {
        // on Windows the process will never return, so we cannot wait
        p.waitFor();
      }
      LOG.trace("call terminated, let us try to connect ...");
    } catch (final Exception x) {
      LOG.trace("failed to start Rserve process with ", x);
      return false;
    }
    /*
     * try up to 5 times before giving up. We can be conservative here, because
     * at this point the process execution itself was successful and the start
     * up is usually asynchronous
     */
    int attempts = 5;

    while (attempts > 0) {
      try {
        final RConnection c = new RConnection();
        LOG.trace("Rserve is running.");
        c.close();
        return true;
      } catch (final Exception e2) {
        LOG.trace("Try failed with: ", e2);
      }
      /* a safety sleep just in case the start up is delayed or asynchronous */
      try {
        Thread.sleep(5000);
      } catch (final InterruptedException ix) {
        LOG.trace(ix.getCause().getMessage());
      }
      attempts--;
    }
    return false;
  }

  /**
   * Checks whether Rserve is running and if that's not the case it attempts to
   * start it using the defaults for the platform where it is run on. This
   * method is meant to be set-and-forget and cover most default setups.
   *
   * <p>
   * For special setups you may get more control over R with < <code>launchRserve</code> instead.
   */
  public static boolean checkLocalRserve() {
    if (isRserveRunning()) {
      return true;
    }
    final String osname = System.getProperty("os.name");
    if (osname != null && osname.length() >= 7 && osname.substring(0, 7).equals("Windows")) {
      LOG.trace("Windows: query registry to find where R is installed ...");
      String installPath = null;
      try {
        final Process rp = Runtime.getRuntime().exec("reg query HKLM\\Software\\R-core\\R");
        final StreamHog regHog = new StreamHog(rp.getInputStream(), true);
        rp.waitFor();
        regHog.join();
        installPath = regHog.getInstallPath();
      } catch (final Exception rge) {
        LOG.trace("ERROR: unable to run REG to find the location of R: ", rge);
        return false;
      }
      if (installPath == null) {
        LOG.trace("ERROR: canot find path to R. Make sure reg is available and R was installed with registry settings.");
        return false;
      }
      return launchRserve(installPath + "\\bin\\R.exe");
    }
    /* try some common unix locations of R */
    return launchRserve("R") || new File("/Library/Frameworks/R.framework/Resources/bin/R").exists()
        && launchRserve("/Library/Frameworks/R.framework/Resources/bin/R")
        || new File("/usr/local/lib/R/bin/R").exists() && launchRserve("/usr/local/lib/R/bin/R")
        || new File("/usr/lib/R/bin/R").exists() && launchRserve("/usr/lib/R/bin/R")
        || new File("/usr/local/bin/R").exists() && launchRserve("/usr/local/bin/R") || new File("/sw/bin/R").exists()
        && launchRserve("/sw/bin/R") || new File("/usr/common/bin/R").exists() && launchRserve("/usr/common/bin/R")
        || new File("/opt/bin/R").exists() && launchRserve("/opt/bin/R");
  }

  /**
   * Check if Rserve is currently running (on local machine on default port).
   *
   * @return <code>true</code> if local Rserve instance is running, <code>false</code> otherwise
   */
  public static boolean isRserveRunning() {
    try {
      final RConnection c = new RConnection();
      LOG.debug("Rserve is running.");
      c.close();
      return true;
    } catch (final Exception e) {
      LOG.trace("First connect try failed with: ", e);
      return false;
    }
  }

  /**
   * Shutdown R backend when needed
   *
   * @return <code>true</code> if the backend was shutdown
   */
  public static synchronized boolean shutdownRserve() {

    try {
      final RConnection c = new RConnection();
      c.shutdown();
    } catch (final RserveException x) {
      LOG.trace("Rserve Already down: ", x);
      return true;
    }

    return true;
  }

}
