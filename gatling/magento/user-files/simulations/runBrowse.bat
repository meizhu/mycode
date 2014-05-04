set GATLING_HOME=C:\MageBenchmarks\gatling-1.4.1
set JAVA_OPTS=-DrampTime=1 -Dduration=2 -DnumBrowseProduct=50 -DnumBrowseCatalog=50 -DurlBase=http://benchmark.magento.com/index.php/
%GATLING_HOME%\bin\gatling.bat -s Browse