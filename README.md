Develop: 0.7-SNAPSHOT
---------------------
* Sonar fixes
    - Rethrow exceptions
    - Remove duplicate code
    - finalize classes
    - Reduce String repetition

* Surefire reporting to pom.xml
* CI management, authors to pom

Stable/Master: Release v0.6: git tag: release_0.6
-------------------------------------------------
* Apply AURIN indentation
* Fix Sonar issues
* Rserve shutdown fixes
* REngine dependency updates for RserveAnomalyTest

Release v0.5: https://github.com/AURIN/analytics-commons/tree/master
* Rproperties support class
* Rproperties introspection methods


* Stable/Master branch Release:
Release v0.4: https://github.com/AURIN/analytics-commons/tree/master

* Develop branch Release: v0.5-SNAPSHOT
- R session, connection, compare and print dataframes from 1 or 2 Rconnections 
- The use of loggingCommons R package is deprecated
- Release supports R 3.x versions

Important R dependencies:

1. Install R library 'log4r' globally
sudo R
> install.packages("log4r")
> q() #quit

3. Add the listed packages above to the default R package loader
sudo vim /etc/R/Renviron.site
R_DEFAULT_PACKAGES='utils,grDevices,graphics,stats,classInt,psych,Hmisc,RJSONIO,log4r'


analytics-commons 'rlogging' branch to support statistics 'master/logging' branch

Master branch: development

Stable versions:
Release v 0.1: https://github.com/AURIN/analytics-commons/tree/release_0.1

R Statistical Backend utilities

* R Script Loader as String

* Rserve to start R

