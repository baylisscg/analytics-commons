Building and installing the loggingCommons R package library

1. Building the loggingCommons library

cd analytics-commons/Rpackages/
R CMD build loggingCommons

If the above command is successful, an updated *.tar.gz (loggingCommons_1.0.tar.gz) will be available in the same directory.

2. Installing loggingCommons library for system-wide use
sudo R CMD INSTALL loggingCommons_1.0.tar.gz

3. To confirm working:
Run R from console loading the library

R
> library("loggingCommons")

Run the unit-test for the library

> TestLoggingCommons()

Quit R
> q()
Save workspace image? [y/n/c]: n

