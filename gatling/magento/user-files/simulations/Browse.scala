
import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._
import TestHelperUtils._

class Browse extends Simulation 
{
	
	
//Retrieve system properties for test
	val testDuration=MageUtils.getSysPropAsStr("duration", "4").toInt
	val rampTime=MageUtils.getSysPropAsStr("rampTime", "2").toInt
	val numBrowseProduct=MageUtils.getSysPropAsStr("numBrowseProduct", "50").toInt
	val numBrowseCatalog=MageUtils.getSysPropAsStr("numBrowseCatalog", "50").toInt
	

//Define Scenario			
	val browseProductScn = scenario("Browse Product")
		.during(testDuration minutes){
			MageCheckoutChains.chainBrowseProduct
		}
	val browseCatalogScn = scenario("Browse Catalog")
		.during(testDuration minutes){
			MageCheckoutChains.chainBrowseCatalog
		}
	
	
	
	println("***Start testing Browse Scenario***")
		
	setUp(browseProductScn.users(numBrowseProduct)
		.protocolConfig(MageHeaders.httpConf))
	setUp(browseCatalogScn.users(numBrowseCatalog)
		.protocolConfig(MageHeaders.httpConf))		
	
}
