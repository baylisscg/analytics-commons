
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

