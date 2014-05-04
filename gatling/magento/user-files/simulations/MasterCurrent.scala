import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._

class MasterCurrent extends Simulation {

                /** 
                 * baseUrl, modify this to point to the target magento instance
                */
                val baseUrl = "http://magento.local.com/magento"
                /** 
                 * origin host, used in ajax header
                */
                val origin = "http://magento.local.com"
                /** 
                 * origin host, used in ajax header
                */
                val pauseTime = 50 milliseconds

                val httpConf = httpConfig
                                                .baseURL(baseUrl)
                                                .acceptHeader("*/*")
                                                .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.3")
                                                .acceptLanguageHeader("en-US,en;q=0.8")
                                                .acceptEncodingHeader("gzip,deflate,sdch")
                                                .userAgentHeader("Mozilla/5.0 (Windows NT 5.2; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11")
                                                


                val headers_get = Map(
                                                "Accept" -> """text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""",
                                                "Cache-Control" -> """no-cache""",
                                                "Pragma" -> """no-cache"""
                )

                val headers_post = Map(
                                                "Accept" -> """text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""",
                                                "Cache-Control" -> """max-age=0""",
                                                "Content-Type" -> """application/x-www-form-urlencoded""",
                                                "Origin" -> origin
                )

                val headers_get_no_cache = Map(
                                                "Accept" -> """text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""",
                                                "Cache-Control" -> """max-age=0"""
                )

                val headers_ajax = Map(
                                                "Accept" -> """text/javascript, text/html, application/xml, text/xml, */*""",
                                                "X-Prototype-Version" -> """1.7""",
                                                "X-Requested-With" -> """XMLHttpRequest"""
                )

                val headers_ajax_origin = Map(
                                                "Accept" -> """text/javascript, text/html, application/xml, text/xml, */*""",
                                                "Content-type" -> """application/x-www-form-urlencoded; charset=UTF-8""",
                                                "Origin" -> origin,
                                                "X-Prototype-Version" -> """1.7""",
                                                "X-Requested-With" -> """XMLHttpRequest"""
                )

                val oneItemGuestCheckout =
                                exec(http("Index Page")
                                                                                .get("/index.php")
                                                                                .headers(headers_get)
                                                )
                                .pause(pauseTime)
                                .exec(http("Browse Cell Phone Category")
                                                                                .get("/index.php/catalog/category/view/s/cell-phones/id/8/")
                                                                                .headers(headers_get)
                                                )
                                .pause(pauseTime)
                                // save the actionUrl, which is of the form 
                                .exec(http("View Product")
                                                                                .get("/index.php/catalog/product/view/id/166/category/8/")
                                                                                .headers(headers_get)
                                                                                .check(
                                                                                                regex("""checkout/cart/add/uenc/(.*)/\"""")
                                                                                                                .saveAs("actionUrl"))
                                                )
                                .pause(pauseTime)
                                .exec(http("Add to Cart")
                                                                                .post("/index.php/checkout/cart/add/uenc/".concat("${actionUrl}"))
                                                                                .headers(headers_post)
                                                                                                .param("""product""", """166""")
                                                                                                .param("""related_product""", """""")
                                                                                                .param("""qty""", """1""")
                                                )
                                .pause(pauseTime)
                                .exec(http("Cart")
                                                                                .get("/index.php/checkout/cart/")
                                                                                .headers(headers_get_no_cache)
                                                )
                                .exec(http("Checkout Onepage")
                                                                                .get("/index.php/checkout/onepage/")
                                                                                .headers(headers_get)
                                                )
                                .pause(pauseTime)
                                .exec(http("Onepage ajax progress billing")
                                                                                .get("/index.php/checkout/onepage/progress/")
                                                                                .headers(headers_ajax)
                                                                                .queryParam("""toStep""", """billing""")
                                                )
                                .pause(pauseTime)
                                .exec(http("OnePage use guest checkout")
                                                                                .post("/index.php/checkout/onepage/saveMethod/")
                                                                                .headers(headers_ajax_origin)
                                                                                                .param("""method""", """guest""")
                                                )
                                .pause(pauseTime)
                                .exec(http("Onepage saveBilling")
                                                                                .post("/index.php/checkout/onepage/saveBilling/")
                                                                                .headers(headers_ajax_origin)
                                                                                                .param("""billing[address_id]""", """""")
                                                                                                .param("""billing[firstname]""", """fname""")
                                                                                                .param("""billing[lastname]""", """lname""")
                                                                                                .param("""billing[company]""", """""")
                                                                                                .param("""billing[email]""", """y@x.com""")
                                                                                                .param("""billing[street][]""", """address""")
                                                                                                .param("""billing[city]""", """albany""")
                                                                                                .param("""billing[region_id]""", """43""")
                                                                                                .param("""billing[region]""", """""")
                                                                                                .param("""billing[postcode]""", """12210""")
                                                                                                .param("""billing[country_id]""", """US""")
                                                                                                .param("""billing[telephone]""", """212-5556666""")
                                                                                                .param("""billing[fax]""", """""")
                                                                                                .param("""billing[customer_password]""", """""")
                                                                                                .param("""billing[confirm_password]""", """""")
                                                                                                .param("""billing[save_in_address_book]""", """1""")
                                                                                                .param("""billing[use_for_shipping]""", """1""")
                                                )
                                .pause(pauseTime)
                                .exec(http("Onepage getAdditional")
                                                                                .post("/index.php/checkout/onepage/getAdditional/")
                                                                                .headers(headers_ajax_origin)
                                                )
                                .pause(pauseTime)
                                .exec(http("Onepage ajax progress shipping method")
                                                                                .get("/index.php/checkout/onepage/progress/")
                                                                                .headers(headers_ajax)
                                                                                .queryParam("""toStep""", """shipping_method""")
                                                )
                                .pause(pauseTime)
                                .exec(http("onepage save shipping method")
                                                                                .post("/index.php/checkout/onepage/saveShippingMethod/")
                                                                                .headers(headers_ajax_origin)
                                                                                                .param("""shipping_method""", """flatrate_flatrate""")
                                                )
                                .pause(pauseTime)
                                .exec(http("onepage progress payment")
                                                                                .get("/index.php/checkout/onepage/progress/")
                                                                                .headers(headers_ajax)
                                                                                .queryParam("""toStep""", """payment""")
                                                )
                                .pause(pauseTime)
                                .exec(http("onepage progress")
                                                                                .get("/index.php/checkout/onepage/progress/")
                                                                                .headers(headers_ajax)
                                                )
                                .pause(pauseTime)
                                .exec(http("Onepage savePayment")
                                                                                .post("/index.php/checkout/onepage/savePayment/")
                                                                                .headers(headers_ajax_origin)
                                                                                .param("""payment[method]""", """checkmo""")
                                                                                .check(
                                                                                                regex("""form_key\\/(.*)\\/', 'http""")
                                                                                                                .saveAs("form_key"))
                                                )
                                .pause(pauseTime)
                                .exec(http("Onepage progress review")
                                                                                .get("/index.php/checkout/onepage/progress/")
                                                                                .headers(headers_ajax)
                                                                                .queryParam("""toStep""", """review""")
                                                )
                                .pause(pauseTime)
                                .exec(http("Onepage saveOrder")
                                                                                .post("/index.php/checkout/onepage/saveOrder/".concat("form_key/").concat("${form_key}"))
                                                                                .headers(headers_ajax_origin)
                                                                                                .param("""payment[method]""", """checkmo""")
                                                )
                                .pause(pauseTime)
                                //save the form_key from the response, the response is in the form
                                .exec(http("success")
                                                                                .get("/index.php/checkout/onepage/success/")
                                                                                .headers(headers_get)
                                                                                .check(regex("""Your order""").exists)
                                                )

                val scn = scenario("Simple Guest Checkout with Check/Money and 1 item")
                                                                .repeat(5) {oneItemGuestCheckout}

                setUp(scn.users(1).protocolConfig(httpConf))

}
