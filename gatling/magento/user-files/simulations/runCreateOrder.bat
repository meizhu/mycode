set GATLING_HOME=C:\MageBenchmarks\gatling-1.4.1
set JAVA_OPTS=-DrampTime=2 -Dduration=1020 -DnumGuestCheckout=20 -DnumRegisteredCheckout=20 -DurlBase=http://impact-current.mperf.magento.com
%GATLING_HOME%\bin\gatling.bat -s CreateOrders