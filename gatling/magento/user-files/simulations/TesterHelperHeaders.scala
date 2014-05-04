package TestHelperUtils


import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._

object MageHeaders {

 
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
				
	val httpConf = httpConfig
		.baseURL(MageCheckoutChains.urlBase)
		.acceptHeader("text/javascript, text/html, application/xml, text/xml, */*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:17.0) Gecko/20100101 Firefox/17.0")
		
	val headers_1 = Map(
		"Accept" ->
	"""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"""
	)

	val headers_2 = Map(
		"Accept" -> 
	"""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""",
		"Content-Type" -> """application/x-www-form-urlencoded"""
	)

	val headers_3 = Map(
		"Accept" -> 
	"""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""",
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
}