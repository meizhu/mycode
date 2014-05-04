
import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._
import TestHelperUtils._

class Checkout extends Simulation 
{
	val numUsers=MageUtils.getSysPropAsStr("users", "1").toInt
	val rampTime=MageUtils.getSysPropAsStr("rampTime", "1").toInt
	var rn2=new scala.util.Random
	val pauseAddToCart=4+(rn2.nextInt(12-6))
	val pauseCheckout1=4+(rn2.nextInt(12-6))
	val pauseCheckout2=4+(rn2.nextInt(12-6))
	val pauseCheckout3=4+(rn2.nextInt(12-6))
	val pauseCheckout4Billing=4+(rn2.nextInt(12-6))
	val pauseCheckout5Shipping=4+(rn2.nextInt(12-6))
	val pauseCheckout6Payment=4+(rn2.nextInt(12-6))
	val pauseCheckout7Order=4+(rn2.nextInt(12-6))
	var scenario_header = (numUsers + " concurrent users in " + rampTime 
			+ " CartAdd " + pauseAddToCart 
			+ " Ckout1 " + pauseCheckout1 
			+ " Ckout2 " + pauseCheckout2 
			+ " Ckout3 " + pauseCheckout3 
			+ " CkBill " + pauseCheckout4Billing 
			+ " CkShip " + pauseCheckout5Shipping 
			+ " CkPaym " + pauseCheckout6Payment 
			+ " CkOrdr " + pauseCheckout7Order )

  println(scenario_header)

  var urlBase = "https://sample-load-current.mperf.magento.com"

	val httpConf = httpConfig
			.baseURL(urlBase)
			.acceptHeader("text/javascript, text/html, application/xml, text/xml, */*")
			.acceptEncodingHeader("gzip, deflate")
			.acceptLanguageHeader("en-US,en;q=0.5")
			.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:17.0) Gecko/20100101 Firefox/17.0")


	val headers_1 = Map(
			"Accept" -> """text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"""
	)

	val headers_2 = Map(
			"Accept" -> """text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""",
			"Content-Type" -> """application/x-www-form-urlencoded"""
	)

	val headers_3 = Map(
			"Accept" -> """text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""",
			"Connection" -> """keep-alive"""
	)

	val headers_4 = Map(
			"Accept" -> """*/*""",
			"Connection" -> """keep-alive"""
	)

	val headers_8 = Map(
			"Accept" -> """image/png,image/*;q=0.8,*/*;q=0.5""",
			"Connection" -> """keep-alive"""
	)

	val headers_16 = Map(
			"Connection" -> """keep-alive""",
			"X-Prototype-Version" -> """1.7""",
			"X-Requested-With" -> """XMLHttpRequest"""
	)

	val headers_17 = Map(
			"Cache-Control" -> """no-cache""",
			"Connection" -> """keep-alive""",
			"Content-Type" -> """application/x-www-form-urlencoded; charset=UTF-8""",
			"Pragma" -> """no-cache""",
			"X-Prototype-Version" -> """1.7""",
			"X-Requested-With" -> """XMLHttpRequest"""
	)

	val scn = scenario("Checkout")
		.exec(http("product page")
		.get(urlBase +
			"/electronics/cell-phones/blackberry-8100-pearl.html") 
		.headers(headers_1)
		)
		.pause(pauseAddToCart)
		.exec(http("click add to cart")
		.post(urlBase + "/checkout/cart/add/uenc/aHR0cDovL3NhbXBsZS1sb2FkLWN1cnJlbnQubXBlcmYubWFnZW50by5jb20vZWxlY3Ryb25pY3MvY2VsbC1waG9uZXMvYmxhY2tiZXJyeS04MTAwLXBlYXJsLmh0bWw_X19fU0lEPVU,/product/17/")
		.headers(headers_2)
		.param("""product""", """17""")
		.param("""related_product""", """""")
		.param("""qty""", """1""")
		)
		.pause(pauseCheckout1)
		.exec(http("click checkout")
					.get("/checkout/onepage/")
					.headers(headers_3)
			)
		.pause(595 milliseconds)
		.exec(http("__centinel.js")
					.get("/js/mage/centinel.js")
					.headers(headers_4)
			)
		.exec(http("__captcha.js")
					.get("/js/mage/captcha.js")
					.headers(headers_4)
			)
		.pause(11 milliseconds)
		.exec(http("__weee.js")
					.get("/js/varien/weee.js")
					.headers(headers_4)
			)
		.exec(http("__accordion.js")
					.get("/js/varien/accordion.js")
					.headers(headers_4)
			)
		.exec(http("__blackberry-8100-pearl-2.jpg")
.get("/media/catalog/product/cache/1/thumbnail/50x/9df78eab33525d08d6e5fb8d27136e95/b/l/blackberry-8100-pearl-2.jpg")
					.headers(headers_8)
			)
		.pause(59 milliseconds)

		.exec(http("__directpost.js")
					.get("/js/mage/directpost.js")
					.headers(headers_4)
			)
		.pause(334 milliseconds)
		.exec(http("__opc-ajax-loader.gif")
					.get("/skin/frontend/enterprise/default/images/opc-ajax-loader.gif")
					.headers(headers_8)
			)
		.exec(http("__btn_window_close.gif")
					.get("/skin/frontend/enterprise/default/images/btn_window_close.gif")
					.headers(headers_8)
			)
		.exec(http("__blackberry-8100-pearl-2.jpg")
					.get("/media/catalog/product/cache/1/thumbnail/75x/9df78eab33525d08d6e5fb8d27136e95/b/l/blackberry-8100-pearl-2.jpg")
					.headers(headers_8)
			)
		.pause(72 milliseconds)
		.exec(http("__opcheckout.js")
					.get("/skin/frontend/enterprise/default/js/opcheckout.js")
					.headers(headers_4)
			)
		.exec(http("__cvv.gif")
					.get("/skin/frontend/enterprise/default/images/cvv.gif")
					.headers(headers_8)
			)
		.pause(462 milliseconds)
		.exec(http("__bkg_active-step.gif")
					.get("/skin/frontend/enterprise/default/images/bkg_active-step.gif")
					.headers(headers_8)
			)
		.pause(pauseCheckout2)
		.exec(http("/checkout/../progress/")
					.get("/checkout/onepage/progress/")
					.headers(headers_16)
					.queryParam("""toStep""", """billing""")
			)
		.pause(335 milliseconds)
		.exec(http("saveMethod")
					.post("/checkout/onepage/saveMethod/")
					.headers(headers_17)
						.param("""method""", """guest""")
			)
		.pause(pauseCheckout3)
		.exec(http("__icon_ok.gif")
					.get("/skin/frontend/enterprise/default/images/icon_ok.gif")
					.headers(headers_8)
			)
		.pause(pauseCheckout4Billing)
		.exec(http("saveBilling")
					.post("/checkout/onepage/saveBilling/")
					.headers(headers_17)
						.param("""billing[address_id]""", """""")
						.param("""billing[firstname]""", """John""")
						.param("""billing[lastname]""", """Doe""")
						.param("""billing[company]""", """""")
						.param("""billing[email]""", """test@ebay.com""")
						.param("""billing[street][]""", """2211 N 1st St""")
						.param("""billing[city]""", """San Jose""")
						.param("""billing[region_id]""", """12""")
						.param("""billing[region]""", """""")
						.param("""billing[postcode]""", """95131""")
						.param("""billing[country_id]""", """US""")
						.param("""billing[telephone]""", """555-123-1234""")
						.param("""billing[fax]""", """""")
						.param("""billing[customer_password]""", """""")
						.param("""billing[confirm_password]""", """""")
						.param("""billing[save_in_address_book]""", """1""")
						.param("""billing[use_for_shipping]""", """1""")
			)
		.pause(731 milliseconds)
		.exec(http("getAdditional")
					.post("/checkout/onepage/getAdditional/")
					.headers(headers_17)
			)
		.pause(402 milliseconds)
		.exec(http("__bkg_block.gif")
					.get("/skin/frontend/enterprise/default/images/bkg_block.gif")
					.headers(headers_8)
			)
		.pause(279 milliseconds)
		.exec(http("/checkout/../progress/")
					.get("/checkout/onepage/progress/")
					.headers(headers_16)
					.queryParam("""toStep""", """shipping_method""")
			)
		.pause(pauseCheckout5Shipping)
		.exec(http("saveShippingMethod")
					.post("/checkout/onepage/saveShippingMethod/")
					.headers(headers_17)
						.param("""shipping_method""", """flatrate_flatrate""")
						.param("""giftoptions[177][type]""", """quote""")
						.param("""giftoptions[179][type]""", """quote_item""")
			)
		.pause(685 milliseconds)
		.exec(http("/checkout/../progress/")
					.get("/checkout/onepage/progress/")
					.headers(headers_16)
					.queryParam("""toStep""", """payment""")
			)
		.pause(pauseCheckout6Payment)
		.exec(http("savePayment")
					.post("/checkout/onepage/savePayment/")
					.headers(headers_17)
						.param("""payment[method]""", """checkmo""")
			)
		.pause(393 milliseconds)
		.exec(http("__bkg_thead.gif")
					.get("/skin/frontend/enterprise/default/images/bkg_thead.gif")
					.headers(headers_8)
			)
		.pause(316 milliseconds)
		.exec(http("/checkout/../progress/")
					.get("/checkout/onepage/progress/")
					.headers(headers_16)
					.queryParam("""toStep""", """review""")
			)
		.pause(pauseCheckout7Order)
		.exec(http("saveOrder")
					.post("/checkout/onepage/saveOrder/")
					.headers(headers_17)
						.param("""payment[method]""", """checkmo""")
			)
		.pause(804 milliseconds)
		.exec(http("/checkout/../success/")
					.get("/checkout/onepage/success/")
					.headers(headers_3)
			)

	setUp(scn.users(numUsers)
			.ramp(rampTime)
			.protocolConfig(httpConf)
		)
}
