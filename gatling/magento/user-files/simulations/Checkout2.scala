
import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._
import TestHelperUtils._


class Checkout2 extends Simulation 
{
		
	val chainCheckoutGuest=repeat(3) {
			exec(MageCheckoutChains.chainBrowseProduct) 
			.exec(MageCheckoutChains.chainAddToCart) 
		}
		.exec(MageCheckoutChains.chainCheckoutStart)
		.exec(MageCheckoutChains.chainCheckoutGuest)
		.pause(7 seconds, 12 seconds)
		.exec(MageCheckoutChains.chainCheckoutSaveBillingGuest)
		.pause(7 seconds, 12 seconds)
		.exec(MageCheckoutChains.chainCheckoutShippingFlatrate)
		.pause(7 seconds, 12 seconds)
		.exec(MageCheckoutChains.chainCheckoutPaymentCheckmo)
		.exec(http("/checkout/../success/")
			.get("/checkout/onepage/success/")
			.headers(MageHeaders.headers_3)
		)
		
	val chainCheckoutRegistered=repeat(3) {
			exec(MageCheckoutChains.chainBrowseProduct) 
			.exec(MageCheckoutChains.chainAddToCart) 
		}
		.exec(MageCheckoutChains.chainCheckoutStart)
		.exec(MageCheckoutChains.chainCheckoutRegisteredUser)
		.pause(7 seconds, 12 seconds)
		.exec(MageCheckoutChains.chainCheckoutShippingFlatrate)
		.pause(7 seconds, 12 seconds)
		.exec(MageCheckoutChains.chainCheckoutPaymentCheckmo)
		.exec(http("/checkout/../success/")
			.get("/checkout/onepage/success/")
			.headers(MageHeaders.headers_3)
		)
	
	val chainCheckoutAbandoned=exec(MageCheckoutChains.chainBrowseProduct)
		.pause(12 seconds, 24 seconds)
		.exec(MageCheckoutChains.chainAddToCart)
		.pause(5 seconds, 20 seconds)
		.exec(MageCheckoutChains.chainCheckoutStart)
		.pause(5 seconds, 20 seconds)
		.exec(MageCheckoutChains.chainCheckoutAbandoningUser)

//Retrieve system properties for test
	val testDuration=MageUtils.getSysPropAsStr("duration", "4").toInt
	val rampTime=MageUtils.getSysPropAsStr("rampTime", "2").toInt
	val numBrowseOnly=MageUtils.getSysPropAsStr("numBrowseOnly", "29").toInt
	val numGuestCheckout=MageUtils
		.getSysPropAsStr("numGuestCheckout", "3").toInt
	val numRegisteredCheckout=MageUtils
		.getSysPropAsStr("numRegisteredCheckout", "3").toInt
	val numAbandoned=MageUtils
		.getSysPropAsStr("numAbandoned", "65").toInt

//Define Scenario			
	val browserScn = scenario("Browse Only")
		.during(testDuration minutes){
			MageCheckoutChains.chainBrowseProduct
		}
	val checkoutAbandonedScn = scenario("Checkout Abandoned")
		.during(testDuration minutes){
			chainCheckoutAbandoned
		}
	
	val checkoutGuestScn = scenario("Guest Checkout Scn")
		.during(testDuration minutes){
			chainCheckoutGuest
		}
		
	val checkoutRegisteredScn = scenario("Registered Checkout Scn")
		.during(testDuration minutes){
			chainCheckoutRegistered
		}	
		
	
	println("***Start testing***")
		
	setUp(browserScn.users(numBrowseOnly).ramp(rampTime)
		.protocolConfig(MageHeaders.httpConf))
	setUp(checkoutAbandonedScn.users(numAbandoned).ramp(rampTime)
		.protocolConfig(MageHeaders.httpConf))		
	setUp(checkoutGuestScn.users(numGuestCheckout).ramp(rampTime)
		.protocolConfig(MageHeaders.httpConf))
	
	setUp(checkoutRegisteredScn.users(numRegisteredCheckout).ramp(rampTime)
		.protocolConfig(MageHeaders.httpConf)
	)
}
