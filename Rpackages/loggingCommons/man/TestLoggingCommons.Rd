\name{TestLoggingCommons}
\alias{TestLoggingCommons}
%- Also NEED an '\alias' for EACH other topic documented here.
\title{
%%  ~~function to do ... ~~
Test the logging framework
}
\description{
%%  ~~ A concise (1-5 lines) description of what the function does. ~~
Sets up default logging framework and the setup of default LOG_LEVEL and LOG_DIRECTORY
}
\usage{
TestLoggingCommons()
}
%- maybe also 'usage' for other objects documented here.
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
function () 
{
    if (libraryError() == FALSE) {
        optionsLogging <<- data.frame(LOG_LEVEL = 5, LOG_DIRECTORY = "/tmp")
        rLog <- setupLogging(optionsLogging)
        print("rLog = ")
        print(rLog)
        debug(rLog, "DEBUG:: Hello from TestLoggingCommons()")
        info(rLog, "INFO:: Hello from TestLoggingCommons()")
    }
    else {
        print("Error loading library: log4r")
    }
  }
}
% Add one or more standard keywords, see file 'KEYWORDS' in the
% R documentation directory.
\keyword{ ~kwd1 }
\keyword{ ~kwd2 }% __ONLY ONE__ keyword per line
