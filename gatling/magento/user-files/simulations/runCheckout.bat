REM This file is used to run the Checkout with Targeted Completion scenario
REM You will need to adjust this script to run on Linux, etc.
REM E.g. %GATLING_HOME% will need to be set to $GATLING_HOME
REM rampTime: in seconds, duration: test duration in minutes, numBrowseOnly, numGuestCheckout, numRegisteredCheckout, numAbondoned 
REM are number of users used in each scenario.
set GATLING_HOME=C:\MageBenchmarks\gatling-1.4.1
set JAVA_OPTS=-DrampTime=60 -Dduration=10 -DnumBrowseOnly=29 -DnumGuestCheckout=3 -DnumRegisteredCheckout=3 -DnumAbandoned=65 -DurlBase=http://benchmark.magento.com/
%GATLING_HOME%\bin\gatling.bat -s Checkout2

REM Pre-Steps: run createuser-v2.php and use it to generate two files 
REM in the user-files/data directory: registered_shoppers.csv and 
REM abandoning_shoppers.csv. 
