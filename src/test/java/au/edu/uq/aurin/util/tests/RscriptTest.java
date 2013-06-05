package au.edu.uq.aurin.util.tests;


import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Assert;
import org.junit.Test;

import au.edu.uq.aurin.util.Rscript;

public class RscriptTest {

	@Test
	public void test() {
		
		System.out.println("Rscript Unit Test");
		
		try {
			String script = Rscript.load("/au/edu/uq/aurin/rscripts/script.r");
			Assert.assertNotNull("Unable to load given R script", script);
			
//			System.out.println(System.getProperties().toString()); 
//	        ClassLoader cl = ClassLoader.getSystemClassLoader();
//	        URL[] urls = ((URLClassLoader)cl).getURLs();
//	        for(URL url: urls){
//	        	System.out.println(url.getFile());
//	        }
	        
		} catch (IOException e) {
			Assert.fail(e.getMessage());
//			e.printStackTrace();
		}
		
	}

}
