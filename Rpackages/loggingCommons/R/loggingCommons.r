## Common logging framework setup
# Source this file and then call the setupLogging method with a dataFrame input optionsLogging
# For usage see: TestLoggingCommons() method

## Log Level values to use for the logger: 5=DEBUG, 4=INFO 3=WARN, 2=ERROR, 1=FATAL
## list(OFF=1, ERROR=2, WARN=3, INFO=4, DEBUG=5, TRACE=6)
## Internal only:
# Log Level verbosity method values are: 1=DEBUG, 2=INFO 3=WARN, 4=ERROR, 5=FATAL

## Trace=6 is unsupported
logListLevels <- list(OFF=1, ERROR=2, WARN=3, INFO=4, DEBUG=5, TRACE=6)

libraryError <- function() {
	  errorLibraries <- FALSE
	  
	  if(!require("log4r")) {
	    errorLibraries <- TRUE
	  }
	  print('errorLibraries = '); print(errorLibraries)
	  return(errorLibraries)
}

## This is the method that needs to be called after sourcing the script, see TestLoggingCommons()
setupLogging <- function(optionsLogging=NULL) {

    # Drop stringsAsFactors in DataFrame: optionsLogging using paste()

    if(is.null(optionsLogging)) {
        ## No logger scenario
        print("NO Logging");
        rlogger <- create.logger(logfile="");
        print("Logging to CONSOLE: ")
        level(rlogger) <- verbosity(1) # log level 5 - FATAL or 6 - TRACE only
    } else if(is.numeric(optionsLogging$LOG_LEVEL) == TRUE) {
        print(paste("Logging ENABLED, LOG_LEVEL = ", optionsLogging$LOG_LEVEL))
        lFile <- paste(
                    optionsLogging$LOG_DIRECTORY, 
                    .Platform$file.sep, 
                    Sys.Date(), 
                    ".rlog", 
                    sep="", 
                    collapse="")
        print(paste("Logging FILE: ", lFile))
        rlogger <- create.logger(logfile=lFile)
        level(rlogger) <- verbosity(optionsLogging$LOG_LEVEL)
    } else if(is.character(paste(optionsLogging$LOG_LEVEL, sep="", collapse="")) == TRUE) {
        ## Textual log level specified
        print(paste("Logging ENABLED, 'Character' LOG LEVEL =", optionsLogging$LOG_LEVEL))
        numLogLevel <- logListLevels[[toupper(paste(optionsLogging$LOG_LEVEL, sep="", collapse=""))]]
        print(paste("Logging ENABLED, 'Numeric converted' LOG LEVEL =", numLogLevel))
        if(is.null(numLogLevel) == TRUE) {
            numLogLevel <- 1 # same as no logging if NULL
        }
        ## setup the logger
        lFile <- paste(
                    optionsLogging$LOG_DIRECTORY, 
                    .Platform$file.sep, 
                    Sys.Date(), 
                    ".rlog", 
                    sep="", 
                    collapse="")
        print(paste("Logging FILE: ", lFile))
        rlogger <- create.logger(logfile=lFile)
        print("numLogLEVEL = "); print(numLogLevel)
        print("str(numLogLevel) = "); print(str(numLogLevel))
 #       print('str(getAnywhere("LEVELS")) = '); print(str(getAnywhere("LEVELS")))
        level(rlogger) <- verbosity(numLogLevel)
    } else {
        stop("ERROR: UNKNOWN LOGGER STATE. Should not have happened")
    }

    info(rlogger, c("str(rlogger) = ", capture.output(str(rlogger))))

    print(paste("Logging LEVEL:", optionsLogging$LOG_LEVEL, ", Logging DIRECTORY: ", optionsLogging$LOG_DIRECTORY))
    return(rlogger);
}

## Lower number for more debug output
TestLoggingCommons <- function() {

    # default value
    optionsLogging <<- NULL

	if(libraryError() == FALSE) {
        # 1. Case where logging is defined
		optionsLogging <<- data.frame(LOG_LEVEL=5, LOG_DIRECTORY="/tmp")
		rLog <- setupLogging(optionsLogging)
		print('rLog = '); print(rLog)
		debug(rLog, "DEBUG:: LOG_LEVEL in INTEGER format in TestLoggingCommons()")
		info(rLog, "INFO:: LOG_LEVEL in INTEGER format in TestLoggingCommons()")

        # 2. Case where no logging is defined
        optionsLogging <- NULL
        rm(optionsLogging) # exists(optionsLogging) == FALSE
		rLog <- setupLogging()
		print('NO LOGGING: rLog = '); print(rLog)
		debug(rLog, "NO LOGGING: DEBUG:: from TestLoggingCommons()")
		info(rLog, "NO LOGGING: INFO:: from TestLoggingCommons()")

        # 3. Case of input log level being a String/Char.array
		optionsLogging <<- data.frame(LOG_LEVEL="DEBUG", LOG_DIRECTORY="/tmp")
		rLog <- setupLogging(optionsLogging)
        print('rLog = '); print(rLog)
		debug(rLog, "DEBUG:: LOG_LEVEL in TEXT format in TestLoggingCommons()")
		info(rLog, "INFO:: LOG_LEVEL in TEXT format in TestLoggingCommons()")	

	} else {
		stop("Error loading library: log4r. Please install R package log4r")
	}
}

## Load the dependent libraries
libraryError()

## call TestLogging
#TestLoggingCommons()
