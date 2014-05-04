package TestHelperUtils

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._
import TestHelperUtils._

object MageCheckoutChains {
	
	var urlBase = MageUtils.getSysPropAsStr("urlBase", "http://127.0.0.1")
	val useFormKey=MageUtils.shouldUseFormKey();
	val prodFeeder1=csv("products.csv").circular
	val userFeeder=csv("registered_shoppers.csv").circular
	val abandoningUserFeeder=csv("abandoning_shoppers.csv").circular
	val catalogFeeder=csv("catalog.csv").circular
	
	
	val chainBrowseCatalog=feed(catalogFeeder)
		.exec(http("Catalog Page")
			  .get(urlBase +"${catalogUrl}")
			  .headers(MageHeaders.headers_1)
			  ).pause (5 seconds, 12 seconds)
			  
		  
	var chainBrowseProduct=feed(prodFeeder1)
		.exec(http("product page")
			.get(urlBase + "${productUrl}") 
			.headers(MageHeaders.headers_1)
		);
	var addProductUrl = urlBase + "/checkout/cart/add/product/" 
				+ "${productId}" + "/";		
		if(useFormKey){
			chainBrowseProduct = chainBrowseProduct.check(regex("""checkout/cart/add/uenc/(.*)/\"""").saveAs("actionUrl"));
             addProductUrl =  urlBase +  "/index.php/checkout/cart/add/uenc/".concat("${actionUrl}");                                                                                              
		}
		
	
	val chainAddToCart=
		exec(http("Click add to cart")
			.post(addProductUrl)
			.headers(MageHeaders.headers_2)
			.param("""product""", """${productId}""")
			.param("""related_product""", """""")
			.param("""qty""", """1""")
		)
		.pause(7 seconds, 12 seconds)
	
	val chainCheckoutStart=
		exec(http("click checkout")
			.get("/checkout/onepage/")
			.headers(MageHeaders.headers_3)
		)
		.pause(7 seconds, 12 seconds)
		.exec(http("Checkout progress 1")
			.get("/checkout/onepage/progress/")
			.headers(MageHeaders.headers_16)
			.queryParam("""toStep""", """billing""")
		)
		.pause(335 milliseconds)
	
	val chainCheckoutGuest=
		exec(http("Guest Checkout start")
			.post("/checkout/onepage/saveMethod/")
			.headers(MageHeaders.headers_17)
			.param("""method""", """guest""")
		)
	
	val chainCheckoutAbandoningUser=feed(abandoningUserFeeder)
		.exec(http("Registered Checkout:Login and Pick Address")
			.post("/customer/account/loginPost/")
			.headers(MageHeaders.headers_17)
			.param("""login[username]""", """${shopperEmail}""")
			.param("""login[password]""", """${shopperPass}""")
			.param("""context""", """checkout""")
		)
		.pause(20 seconds, 40 seconds)
	
	val chainCheckoutRegisteredUser=feed(userFeeder)
		.exec(http("Registered Checkout:Login and Pick Address")
			.post("/customer/account/loginPost/")
			.headers(MageHeaders.headers_17)
			.param("""login[username]""", """${shopperEmail}""")
			.param("""login[password]""", """${shopperPass}""")
			.param("""context""", """checkout""")
		)
		.pause(4 seconds,10 seconds)
		.exec(http("Registered:Pick Billing Address 1")
			.post("/checkout/onepage/saveBilling/")
			.headers(MageHeaders.headers_17)
			.param("""billing_address_id""", """${addressId}""")
			.param("""billing[address_id]""", """${addressId}""")
			.param("""billing[firstname]""", """Shopper""")
			.param("""billing[lastname]""", """Mage-Gatling""")
			.param("""billing[company]""", """""")
			.param("""billing[street][]""", """7700 W Parmer Ln""")
			.param("""billing[street][]""", """""")
			.param("""billing[city]""", """Austin""")
			.param("""billing[region_id]""", """57""")
			.param("""billing[region]""", """""")
			.param("""billing[postcode]""", """78729""")
			.param("""billing[country_id]""", """US""")
			.param("""billing[telephone]""", """5126914400""")
			.param("""billing[fax]""", """""")
			.param("""billing[save_in_address_book]""", """1""")
			.param("""billing[use_for_shipping]""", """1""")
		)
		.exec(http("Registered:Pick Billing Address 2-1")
			.post("/checkout/onepage/getAdditional/")
			.headers(MageHeaders.headers_17)
		)
		.pause(402 milliseconds)
		.exec(http("Registered:Pick Billing Address 2-2")
			.post("/checkout/onepage/getAdditional/")
			.headers(MageHeaders.headers_17)
		)
		.pause(402 milliseconds)
		.exec(http("Registered:Go to pick Shipping Method")
			.get("/checkout/onepage/progress/")
			.headers(MageHeaders.headers_16)
			.queryParam("""toStep""", """shipping_method""")
		)
		.pause(7 seconds, 12 seconds)
	
	val chainCheckoutSaveBillingGuest=
		exec(http("Guest:Pick Billing Address 1")
			.post("/checkout/onepage/saveBilling/")
			.headers(MageHeaders.headers_17)
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
		.exec(http("Guest:Pick Billing Address 2")
			.post("/checkout/onepage/getAdditional/")
			.headers(MageHeaders.headers_17)
		)
		.pause(402 milliseconds)
		.exec(http("Guest:Go to pick Shipping Method")
			.get("/checkout/onepage/progress/")
			.headers(MageHeaders.headers_16)
			.queryParam("""toStep""", """shipping_method""")
		)
		.pause(7 seconds, 12 seconds)
	
	val chainCheckoutShippingFlatrate=
		exec(http("Set Shipping Method (Flatrate) 1")
			.post("/checkout/onepage/saveShippingMethod/")
			.headers(MageHeaders.headers_17)
			.param("""shipping_method""", """flatrate_flatrate""")
			.param("""giftoptions[177][type]""", """quote""")
			.param("""giftoptions[179][type]""", """quote_item""")
		)
		.pause(7 seconds, 12 seconds)
		.exec(http("Set Shipping Method (Flatrate): Goto Payment")
			.get("/checkout/onepage/progress/")
			.headers(MageHeaders.headers_16)
			.queryParam("""toStep""", """payment""")
		)
	
	val chainCheckoutPaymentCheckmo=
		exec(http("Set Payment Method (Check/MO) 1")
			.post("/checkout/onepage/savePayment/")
			.headers(MageHeaders.headers_17)
			.param("""payment[method]""", """checkmo""")
		)
		.pause(393 milliseconds)
		.exec(http("Set Payment Method (Check/MO) 2")
			.get("/checkout/onepage/progress/")
			.headers(MageHeaders.headers_16)
			.queryParam("""toStep""", """review""")
		)
		.pause(7 seconds, 12 seconds)
		.exec(http("Set Payment Method (Check/MO) 3")
			.post("/checkout/onepage/saveOrder/")
			.headers(MageHeaders.headers_17)
			.param("""payment[method]""", """checkmo""")
		)
		.pause(804 milliseconds)		
}