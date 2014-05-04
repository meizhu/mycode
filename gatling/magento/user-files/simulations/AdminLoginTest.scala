package mage 
import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._

class adminLogin extends Simulation {

	val httpConf = httpConfig
			.baseURL("http://mage-latest.loc")
			.acceptHeader("image/png,image/*;q=0.8,*/*;q=0.5")
			.acceptEncodingHeader("gzip, deflate")
			.acceptLanguageHeader("ru,en-us;q=0.7,en;q=0.3")
			.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64; rv:17.0) Gecko/20100101 Firefox/17.0")


	val headers_1 = Map(
			"Accept" -> """text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""",
			"Cache-Control" -> """no-cache""",
			"Pragma" -> """no-cache"""
	)

	val headers_15 = Map(
			"Accept" -> """text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""",
			"Content-Type" -> """application/x-www-form-urlencoded"""
	)


	val scn = scenario("Scenario Name")
		.exec(http("http_mage_latest_loc_index_php_admin")
					.get("http://mage-latest.loc/index.php/admin/")
					.headers(headers_1)
					check(
					regex("""<input name="form_key" type="hidden" value="([a-zA-Z0-9]*)" />""")
					.saveAs("form_key"))
			)
		.exec(http("http_mage_latest_loc_index_php_admin")
					.post("http://mage-latest.loc/index.php/admin/")
					.headers(headers_15)
						.param("""form_key""", """${form_key}""")
						.param("""login[username]""", """kirill""")
						.param("""login[password]""", """123123q""")
			)

	setUp(scn.users(5).protocolConfig(httpConf))
}