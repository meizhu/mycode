
import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._
import TestHelperUtils._


class CreateOrders extends Simulation
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

//Retrieve system properties for test
  val testDuration=MageUtils.getSysPropAsStr("duration", "4").toInt
  val rampTime=MageUtils.getSysPropAsStr("rampTime", "2").toInt
   val numGuestCheckout=MageUtils
    .getSysPropAsStr("numGuestCheckout", "50").toInt
  val numRegisteredCheckout=MageUtils
    .getSysPropAsStr("numRegisteredCheckout", "50").toInt

  //Define Scenario

  val checkoutGuestScn = scenario("Guest Checkout Scn")
    .during(testDuration minutes){
    chainCheckoutGuest
  }

  val checkoutRegisteredScn = scenario("Registered Checkout Scn")
    .during(testDuration minutes){
    chainCheckoutRegistered
  }

   println("***Start CreateOrder testing***")

  setUp(checkoutGuestScn.users(numGuestCheckout).ramp(rampTime)
    .protocolConfig(MageHeaders.httpConf))

  setUp(checkoutRegisteredScn.users(numRegisteredCheckout).ramp(rampTime)
    .protocolConfig(MageHeaders.httpConf)
  )
}
