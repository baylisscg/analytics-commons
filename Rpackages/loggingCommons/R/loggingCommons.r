## Common logging framework setup
# Source this file and then call the setupLogging method with a dataFrame input optionsLogging
# For usage see: TestLoggingCommons() method

## Log Level values to use for the logger: 5=DEBUG, 4=INFO 3=WARN, 2=ERROR, 1=FATAL

## Internal only:
# Log Level verbosity method values are: 1=DEBUG, 2=INFO 3=WARN, 4=ERROR, 5=FATAL

libraryError <- function() {
	  errorLibraries <- FALSE
	  
	  if(!require("log4r")) {
	    errorLibraries <- TRUE
	  }
	  print('errorLibraries = '); print(errorLibraries)
	  return(errorLibraries)
}

## This is the method that needs to be called after sourcing the script, see TestLoggingCommons()
setupLogging <- function(optionsLogging) {

    print(paste("Logging LEVEL:", optionsLogging$LOG_LEVEL, ", Logging DIRECTORY: ", optionsLogging$LOG_DIRECTORY))
    if((optionsLogging$LOG_LEVEL == 0) || (optionsLogging$LOG_LEVEL < 0)){
        # disable logging
        print(paste("Logging DISABLED, LOG_LEVEL = ", optionsLogging$LOG_LEVEL));
        rlogger <- create.logger(logfile="");
        print("Logging to CONSOLE: ")
        level(rlogger) <- verbosity(1) # log level 5 - FATAL only
    } else {
    ## } else if((optionsLogging$LOG_LEVEL >= 1) && (optionsLogging$LOG_LEVEL < 6)) {
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
##    } else {
        ## setup max log level '1' for DEBUG
##        level(rlogger) <- verbosity(optionsLogging$LOG_LEVEL)
    }
    
    info(rlogger, c("str(rlogger) = ", capture.output(str(rlogger))))
    return(rlogger);
}

## Lower number for more debug output
TestLoggingCommons <- function() {

	if(libraryError() == FALSE) {
		optionsLogging <- data.frame(LOG_LEVEL=5, LOG_DIRECTORY="/tmp")
		rLog <- setupLogging(optionsLogging)
		print('rLog = '); print(rLog)
		debug(rLog, "DEBUG:: Hello from TestLoggingCommons()")
		info(rLog, "INFO:: Hello from TestLoggingCommons()")
	} else {
		print("Error loading library: log4r")
	}
}

## Load the dependent libraries
libraryError()

## call TestLogging
#TestLoggingCommons()
