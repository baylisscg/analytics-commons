\name{setupLogging}
\alias{setupLogging}
%- Also NEED an '\alias' for EACH other topic documented here.
\title{
%%  ~~function to do ... ~~
Setup the logging framework
}
\description{
%%  ~~ A concise (1-5 lines) description of what the function does. ~~
}
\usage{
setupLogging(optionsLogging)
}
%- maybe also 'usage' for other objects documented here.
\arguments{
  \item{optionsLogging}{
%%     ~~Describe \code{optionsLogging} here~~
}
}
\details{
%%  ~~ If necessary, more details than the description above ~~
}
\value{
%%  ~Describe the value returned
%%  If it is a LIST, use
%%  \item{comp1 }{Description of 'comp1'}
%%  \item{comp2 }{Description of 'comp2'}
%% ...
}
\references{
%% ~put references to the literature/web site here ~
}
\author{
%%  ~~who you are~~
}
\note{
%%  ~~further notes~~
}

%% ~Make other sections like Warning with \section{Warning }{....} ~

\seealso{
%% ~~objects to See Also as \code{\link{help}}, ~~~
}
\examples{
##---- Should be DIRECTLY executable !! ----
##-- ==>  Define data, use random,
##--	or do  help(data=index)  for the standard data sets.

## The function is currently defined as
function (optionsLogging) 
{
    print(paste("Logging LEVEL:", optionsLogging$LOG_LEVEL, ", Logging DIRECTORY: ", 
        optionsLogging$LOG_DIRECTORY))
    if (optionsLogging$LOG_LEVEL == 0) {
        print(paste("Logging DISABLED, LOG_LEVEL = ", optionsLogging$LOG_LEVEL))
        rlogger <- create.logger(logfile = "")
        print("Logging to CONSOLE: ")
        level(rlogger) <- verbosity(1)
    }
    else {
        print(paste("Logging ENABLED, LOG_LEVEL = ", optionsLogging$LOG_LEVEL))
        lFile <- paste(optionsLogging$LOG_DIRECTORY, .Platform$file.sep, 
            Sys.Date(), ".rlog", sep = "", collapse = "")
        print(paste("Logging FILE: ", lFile))
        rlogger <- create.logger(logfile = lFile)
        level(rlogger) <- verbosity(optionsLogging$LOG_LEVEL)
    }
    info(rlogger, c("str(rlogger) = ", capture.output(str(rlogger))))
    return(rlogger)
  }
}
% Add one or more standard keywords, see file 'KEYWORDS' in the
% R documentation directory.
\keyword{ ~kwd1 }
\keyword{ ~kwd2 }% __ONLY ONE__ keyword per line
