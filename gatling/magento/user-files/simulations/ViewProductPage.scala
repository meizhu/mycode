
import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._

class ViewProductPage extends Simulation {

	val httpConf = httpConfig
			.baseURL("http://sample-load-current.mperf.magento.com")
			.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
			.acceptEncodingHeader("gzip, deflate")
			.acceptLanguageHeader("en-US,en;q=0.5")
			.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:17.0) Gecko/20100101 Firefox/17.0")



	val scn = scenario("View Product Page")
		.exec(http("View Product")
					.get("/electronics/cell-phones/blackberry-8100-pearl.html")
			)

	setUp(scn.users(200).ramp(30).protocolConfig(httpConf))
}
