####### SAMPLE R SCRIPT INTERFACE #######
## Implement all the methods below for an R-Script with the 
## appropriate return value for each method specified.

############ REQUIRED LIBRARIES #########
## Check for the required libraries
## return 'TRUE' in case of error
libraryError <- function() {
  
}

############ SUPPORT METHODS ############
## Check if the number of input arguments is X
## return 'TRUE' in case of error
argsError <- function(numArgs, nArgs) {
  
}

## Check if the input is valid object type
## Make sure to catch the input errors early
## return 'TRUE' in case of error
inputError <- function(iData, iOptions) {
  
}

########## METHOD_NAME STATISTIC COMPUTE ##########
## return 'NULL' in case of error
METHOD_NAME <- function(iData, iOptions) {

	## load the logging commons script
	## this could be done at the start of the script file
	print("START:: Sourcing logging commons")
	location <- paste("util",  .Platform$file.sep, "loggingCommons.r", sep="", collapse="")
	print("OS specific PATH generation"); print(location)
	source(location)
	print("DONE:: Sourcing logging commons")
	
	optionsLogging <- data.frame(LOG_LEVEL=5, LOG_DIRECTORY="/tmp")
	rLog <- setupLogging(optionsLogging)
#	print('METHOD_NAME:: rLog = '); print(rLog)
	print("Now we can use rLog for logging")
	
	debug(rLog, "DEBUG:: Hello using the logger rLog")
	info(rLog, "INFO:: Hello using the logger rLog") 
}

## Test method with sample/dummy data to check working of algorithm
TestMETHOD_NAME <- function() {

	# setup input options
	iData <<- data.frame()
	iOptions <<- data.frame()
	
	METHOD_NAME(iData, iOptions)
}

## TEST RUN
# TESTMETHOD_NAME()

#### R MAIN AUTO RUN
## Auto-run the METHOD_NAME command
# oooo <- source("script.r")
## Sample inputs for testing

## To check TESTMETHOD_NAME() uncomment it above and 
## comment the line below METHOD_NAME(....)
METHOD_NAME(iData, iOptions)

