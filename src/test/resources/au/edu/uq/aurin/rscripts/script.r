####### SAMPLE R SCRIPT INTERFACE #######
## Implement all the methods below for an R-Script with the 
## appropriate return value for each method specified.

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
                              .Platform$file.sep,
                              "rlogger.rlog", sep="", collpse=""))
  tupperLvl <- as.character(toupper(optionsLogging$LOG_LEVEL))
  numLogLevel <- as.integer(logListLevels[[tupperLvl]])
  llvl <- verbosity(numLogLevel)
  print("lfile = "); print(lfile)
  print("tupperLvl = "); print(tupperLvl)
  print("numLogLevel = "); print(numLogLevel)
  print("llvl = "); print(llvl)
  
  rLog <- log4r::create.logger(logfile=lfile, level=llvl)
  #  print(paste("str(rLog) = ", capture.output(str(rLog))))
  #  info(rLog, paste("info:: str(rLog) = ", capture.output(str(rLog))))
  #  debug(rLog, paste("debug:: str(rLog) = ", capture.output(str(rLog))))
  
  return(rLog)
}

# Main logger setup function
# Supports optionsLogging Dataframe with: String LOG_LEVEL and LOG_DIRECTORY
setupLogging <- function() {
  
  libraryError()
  
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

############ SUPPORT METHODS ############
## Library loading methods
libraryError1 <- function(rLog) {
  
  info(rLog, "============= Test Script libraries ============")
  if(!(require("stats") && require("boot") && require("RJSONIO"))) {
    stop("Library Error: ", call.=error(rLog, traceback()))
  }
  ## call the library version checker
  libraryVersionError(rLog)
}

## library version check
libraryVersionError <- function(rLog) {
  
  if(compareVersion(toString(packageVersion("RJSONIO")), "1.0") == -1) {
    error(rLog, paste('Error library version: ', call.=print(traceback())))
    stop("Error Library version: ", call.=error(rLog, traceback()))
  }
}

## Check if the input is valid object type
## Make sure to catch the input errors early
inputError <- function(rLog, dataFrameName, optionsM, optionsLogging) {
  
  if(!(exists("dataFrameName") && exists("optionsM"))) {
    warning(paste("ERROR: Invalid inputs for: dataFrameName = ", dataFrameName, optionsM))
    error(rLog, paste("Invalid inputs: ", dataFrameName, optionsM))
    stop("ERROR: Invalid inputs: ", call.=print(paste(dataFrameName, optionsM)))
  }
  
  if(!(is.character(dataFrameName) && length(dataFrameName) >= 1)) {
    error(rLog, paste("Invalid input, length: dataFrameName = ", dataFrameName))
    stop(paste("ERROR: Invalid input, length: dataFrameName = ", dataFrameName), call.=error(rLog, traceback()))
  }
  dataF <- get(dataFrameName)
  info(rLog, paste("dataF = ", dataF))
  ## check input dataframe
  if(!(is.data.frame(dataF) && length(dataF) >= 1)) {
    error(rLog, paste("Invalid input, length: dataF = ", dataF))
    stop(paste("ERROR: Invalid input, length: dataF = ", dataF), call.=error(rLog, traceback()))
  }
  
  if(!(is.data.frame(optionsM) && length(optionsM) >= 1)) {
    error(rLog, paste("ERROR: Invalid input, length: optionsM = ", optionsM))
  }
  
  if(exists("optionsLogging")) {
    if(!(is.data.frame(optionsLogging) && length(optionsLogging) >= 1)) {
      error("Invalid input, length: optionsLogging = ", optionsLogging)
      stop("ERROR: Logging option INVALID", call.=print(paste("Invalid input, length: optionsLogging = ", optionsLogging)))
    }
  }
  
  debug(rLog, paste("str(dataF) = ", capture.output(str(dataF))))
  debug(rLog, paste("dataF = ", capture.output(dataF)))
  info(rLog, paste("optionsM = ", optionsM))
  
}

########## METHOD_NAME STATISTIC COMPUTE ##########
## return 'NULL' in case of error
METHOD_NAME <- function() {
  
  oData <- NULL
  
  # setup
  #optionsLogging <- data.frame(LOG_LEVEL="DEBUG", LOG_DIRECTORY="/tmp")
  rLog <- setupLogging()
  #  print('METHOD_NAME:: rLog = '); print(rLog)
  print("Now we can use rLog for logging")
  
  libraryError1(rLog)
  inputError(rLog, dataFrameName, optionsM, optionsLogging)
  dataF <- get(dataFrameName)
  # Remove na
  dataF <- na.omit(dataF)
  
  
  debug(rLog, "DEBUG:: Hello using the logger rLog")
  info(rLog, "INFO:: Hello using the logger rLog")
  
  return(oData)
}

## Test method with sample/dummy data to check working of algorithm
TestMETHOD_NAME <- function() {
  
  # setup inputs
  dataF <<- data.frame(
    col1=c(1.1, 2.2, 3.3, 11.1, 22.2, 33.3),
    col2=c(120.0, 250.0, 390.0, 41.0, 56.0, 66.0),
    col3=c(1300.0, 2600.0, 3800.0, 405.0, 550.0, 560.0),
    col4=c(1400.1, 270.2, 130.3, 110.1, 226.2, 339.3)
  )
  dataFrameName <<- "dataF"
  dataF <<- get(dataFrameName)
  
  optionsM <<- data.frame(
    intercept=TRUE
  )
  
  optionsLogging <<- data.frame(LOG_LEVEL="DEBUG", LOG_DIRECTORY="/tmp")
  
  METHOD_NAME()
}

## TEST RUN
# TESTMETHOD_NAME()

#### R MAIN AUTO RUN
## Auto-run the METHOD_NAME command
# oooo <- source("script.r")
## Sample inputs for testing

## To check TESTMETHOD_NAME() uncomment it above and 
## comment the line below METHOD_NAME(....)
METHOD_NAME()

