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
  if( !(require("log4r")) ) {
    stop("Library Error: ", call.=print(traceback()))
  }  
}

# No logging scenario
setupNullLogger <- function() {
  
  lfile <- ""
  llvl <- 1
  rLog <- create.logger(logfile=lfile);
  level(rLog) <- verbosity(llvl) # log level 5 - FATAL or 6 - TRACE only
  
#  info(rLog, c("str(rlogger) = ", capture.output(str(rlogger))))
  print(paste("Logging LEVEL:", llvl, "Logging DIRECTORY:", lfile))
  
  return(rLog)
}

# Logging to given directory and a log level
setupFileLogger <- function(optionsLogging) {
  
  lfile <- as.character(paste(optionsLogging$LOG_DIRECTORY, 
                              .Platform$file.sep, Sys.Date(),
                              ".rlog", sep="", collpse=""))
  tupperLvl <- as.character(toupper(optionsLogging$LOG_LEVEL))
  numLogLevel <- as.integer(logListLevels[[tupperLvl]])
  llvl <- verbosity(numLogLevel)
  print("lfile = "); print(lfile)
  print("tupperLvl = "); print(tupperLvl)
  print("numLogLevel = "); print(numLogLevel)
  print("llvl = "); print(llvl)
  
  rLog <- log4r::create.logger(logfile=lfile, level=llvl)
  print(paste("str(rLog) = ", capture.output(str(rLog))))
  info(rLog, paste("info:: str(rLog) = ", capture.output(str(rLog))))
  debug(rLog, paste("debug:: str(rLog) = ", capture.output(str(rLog))))
  
  return(rLog)
}

# Main logger setup function
# Supports: String LOG_LEVEL and LOG_DIRECTORY
setupLogging <- function() {
  
  #rLog <- setupNullLogger()
  
  # check validity of optionsLogging
  if(exists("optionsLogging") == TRUE) {
    print("optionsLogging provided")
    # validitity check
    if( (typeof(as.character(optionsLogging$LOG_LEVEL)) == typeof("string")) && 
          (typeof(as.character(optionsLogging$LOG_DIRECTORY)) == typeof("string"))) {
      print("VALID 'optionsLogging' provided")
     rLog <- setupFileLogger(optionsLogging)
    } else {
      # No logger Scenario
      print("NO Logging. Logging to Console");
      rLog <- setupNullLogger()
    }
  } else {
    ## No logger scenario
    print("NO Logging. Logging to Console");
    rLog <- setupNullLogger()
  }
  return(rLog)
}


# ## This is the method that needs to be called after sourcing the script, see TestLoggingCommons()
# setupLogging <- function(optionsLogging) {
# 
#   rlogger <- NULL
#     # Drop stringsAsFactors in DataFrame: optionsLogging using paste()
#   optionsLogging <- data.frame(LOG_LEVEL="DEBUG", LOG_DIRECTORY="/tmp")
# #   print(paste("------ BEFORE ------- SETUPLOGGING: str(optionsLogging) = ", optionsLogging))
# # #  ol <- base::rapply(optionsLogging, as.character, classes="factor", how="replace")
# #   ol <- optionsLogging
# #   print(paste("------ OL ATER --------- SETUPLOGGING: ol = ", ol))
# # #   print(paste("------ STR OL --------- str(ol) = ", ol))
# #   print(paste("------ OL LOGFILE --------- SETUPLOGGING: str(ol$logfile) = ", ol$logfile))
# #   print(paste("------ OL LEVEL --------- SETUPLOGGING: str(ol$level) = ", ol$level))
#   
# #  numLogLevel <- as.integer(logListLevels[[toupper(paste(optionsLogging$LOG_LEVEL, sep="", collapse=""))]])
#   numLogLevel <- 5
#   #print(paste("Logging ENABLED, 'Character to Numeric converted' LOG LEVEL =", numLogLevel))
#   
#   lFile <- toString(paste(
#     as.character.factor(optionsLogging$LOG_DIRECTORY), 
#     .Platform$file.sep, 
#     Sys.Date(), 
#     ".rlog", 
#     sep="", 
#     collapse=""))
# #  lFile <- toString(paste("/tmp", .Platform$file.sep, "2013-10-29.rlog", sep="", collapse=""))
#   print(paste("--------- Logging FILE: ", lFile))
#   rlogger <- create.logger(logfile=lFile)
# #   rlogger <- create.logger(logfile="/tmp/2013-10-29.rlog")
#   level(rlogger) <- verbosity(numLogLevel)
#   info(rlogger, paste("str(rlogger) = ", capture.output(str(rlogger))))
# 
#   #print(paste("------ BEFORE ------- SETUPLOGGING: str(optionsLogging) = ", capture.output(str(optionsLogging))))
#   #info(rlogger, paste("str(optionsLogging) = ", capture.output(str(optionsLogging))))
#   
#   #optionsLogging <- base::rapply(optionsLogging, as.character, classes="factor", how="replace")
# #   llvl <- as.character.factor(optionsLogging$LOG_LEVEL)
# #   lf <- as.character.factor(optionsLogging$LOG_DIRECTORY)
# #   print(c("------ AFTER ------- SETUPLOGGING: llvl = ", llvl))
# #   print(c("------ AFTER ------- SETUPLOGGING: lf = ", lf))
# #   info(rlogger, c("------ AFTER ------- SETUPLOGGING: llvl = ", llvl))
# #   info(rlogger, c("------ AFTER ------- SETUPLOGGING: lf = ", lf))
#   
# #   info(rlogger, paste("str(optionsLogging) = ", capture.output(str(optionsLogging))))
#   
#   #  print(paste("======= FINAL Logging LEVEL:", optionsLogging$LOG_LEVEL, ", Logging DIRECTORY: ", optionsLogging$LOG_DIRECTORY))
#   return(rlogger)
#   
# #     if(!is.null(optionsLogging)) {
# #       # we have optionsLogging specified
# #       print("VALID OPTIONSLOGGING")
# #       optionsLogging <- rapply(optionsLogging, as.character, classes="factor", how="replace")
# #       print("REPLACED FACTORS AS CHARACTERS")
# #       #print(paste("------ AFTER ------- SETUPLOGGING: str(optionsLogging) = ", capture.output(str(optionsLogging))))
# #     }
#     
# #     if(is.null(optionsLogging)) {
# #         ## No logger scenario
# #         print("NO Logging");
# #         rlogger <- create.logger(logfile="");
# #         print("Logging to CONSOLE: ")
# #         level(rlogger) <- verbosity(1) # log level 5 - FATAL or 6 - TRACE only
# #         
# #         info(rlogger, c("str(rlogger) = ", capture.output(str(rlogger))))
# #         print(paste("Logging LEVEL:", optionsLogging$LOG_LEVEL, ", Logging DIRECTORY: ", optionsLogging$LOG_DIRECTORY))
# #         return(rlogger)
# #     }
# #   
# #     optionsLogging <- rapply(optionsLogging, as.character, classes="factor", how="replace")
# #   
# #     if(is.numeric(optionsLogging$LOG_LEVEL) == TRUE) {
# #         print(paste("Logging ENABLED, LOG_LEVEL = ", optionsLogging$LOG_LEVEL))
# #         print(paste("Logging ENABLED, LOG_DIRECTORY = ", optionsLogging$LOG_DIRECTORY))
# #         lFile <- paste(
# #                     optionsLogging$LOG_DIRECTORY, 
# #                     .Platform$file.sep, 
# #                     Sys.Date(), 
# #                     ".rlog", 
# #                     sep="", 
# #                     collapse="")
# #         print(paste("Logging FILE: ", lFile))
# #         rlogger <- create.logger(logfile=lFile)
# #         level(rlogger) <- verbosity(optionsLogging$LOG_LEVEL)
# #         
# #         info(rlogger, c("str(rlogger) = ", capture.output(str(rlogger))))
# #         print(paste("Logging LEVEL:", optionsLogging$LOG_LEVEL, ", Logging DIRECTORY: ", optionsLogging$LOG_DIRECTORY))
# #         return(rlogger)
# #     } 
# #     if(is.character(paste(optionsLogging$LOG_LEVEL, sep="", collapse="")) == TRUE) {
# #         ## Textual log level specified
# #         print(paste("Logging ENABLED, 'Character' LOG LEVEL =", optionsLogging$LOG_LEVEL))
# #         numLogLevel <- logListLevels[[toupper(paste(optionsLogging$LOG_LEVEL, sep="", collapse=""))]]
# #         print(paste("Logging ENABLED, 'Numeric converted' LOG LEVEL =", numLogLevel))
# #         print(paste("Logging ENABLED, LOG_DIRECTORY = ", optionsLogging$LOG_DIRECTORY))
# #         
# #         if(is.null(numLogLevel) == TRUE) {
# #             numLogLevel <- 1 # same as no logging if NULL
# #         }
# #         ## setup the logger
# #         lFile <- paste(
# #                     optionsLogging$LOG_DIRECTORY, 
# #                     .Platform$file.sep, 
# #                     Sys.Date(), 
# #                     ".rlog", 
# #                     sep="", 
# #                     collapse="")
# #         print(paste("Logging FILE: ", lFile))
# #         rlogger <- create.logger(logfile=lFile)
# #         print("numLogLEVEL = "); print(numLogLevel)
# #         print("str(numLogLevel) = "); print(str(numLogLevel))
# #         print('str(getAnywhere("LEVELS")) = '); print(str(getAnywhere("LEVELS")))
# #         print('str(getAnywhere("LEVEL")) = '); print(str(getAnywhere("LEVEL")))
# #         level(rlogger) <- verbosity(numLogLevel)
# #  
# #         info(rlogger, c("str(rlogger) = ", capture.output(str(rlogger))))
# #         print(paste("Logging LEVEL:", optionsLogging$LOG_LEVEL, ", Logging DIRECTORY: ", optionsLogging$LOG_DIRECTORY))
# #         return(rlogger)
# #     } else {
# #         stop("ERROR: UNKNOWN LOGGER STATE. Should not have happened")
# #     }
# 
# #    info(rlogger, c("str(rlogger) = ", capture.output(str(rlogger))))
# #    print(paste("Logging LEVEL:", optionsLogging$LOG_LEVEL, ", Logging DIRECTORY: ", optionsLogging$LOG_DIRECTORY))
# #    return(rlogger)
# }
# 
# ## Lower number for more debug output
# TestLoggingCommons <- function() {
# 
#     # default value
#     optionsLogging <<- NULL
# 
# 	if(libraryError() == FALSE) {
#     # 1. Case where logging is defined
# 		optionsLogging <- data.frame(LOG_LEVEL=5, LOG_DIRECTORY="/tmp")
# 		rLog <- setupLogging(optionsLogging)
# 		print('rLog = '); print(rLog)
# 		#debug(rLog, "DEBUG:: LOG_LEVEL in INTEGER format in TestLoggingCommons()")
# 		info(rLog, "INFO:: LOG_LEVEL in INTEGER format in TestLoggingCommons()")
# 
#     # 2. Case where no logging is defined
#     optionsLogging <- NULL
#     rm(optionsLogging) # exists(optionsLogging) == FALSE
# 		rLog <- setupLogging()
# 		print('NO LOGGING: rLog = '); print(rLog)
# 		#debug(rLog, "NO LOGGING: DEBUG:: from TestLoggingCommons()")
# 		info(rLog, "NO LOGGING: INFO:: from TestLoggingCommons()")
# 
#     # 3. Case of input log level being a String/Char.array
# 		optionsLogging <- data.frame(LOG_LEVEL="DEBUG", LOG_DIRECTORY="/tmp")
# 		rLog <- setupLogging(optionsLogging)
#     print('rLog = '); print(rLog)
# 		#debug(rLog, "DEBUG:: LOG_LEVEL in TEXT format in TestLoggingCommons()")
# 		info(rLog, "INFO:: LOG_LEVEL in TEXT format in TestLoggingCommons()")	
# 
# 	} else {
# 		stop("Error loading library: log4r. Please install R package log4r")
# 	}
# }

## Load the dependent libraries
libraryError()

## call TestLogging
#TestLoggingCommons()
