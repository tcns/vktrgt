import java.io.InputStream
import java.util.Calendar

import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Created by TIMUR on 04.09.2016.
 */
class UserLoadTest extends Simulation {
    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://vktrgt.herokuapp.com"""

    val httpConf = http
        .baseURL(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("ru,ru-ru;q=0.8,en-us;q=0.5,en;q=0.3")
        .connection("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "X-CSRF-TOKEN" -> "${csrf_token}"
    )
    val headers_http_authenticated_undf = Map(
        "Content-Type" -> """multipart/form-data""",
        "X-CSRF-TOKEN" -> "${csrf_token}"
    )

    val stream : InputStream = getClass.getResourceAsStream("/readme.txt")
    val lines = scala.io.Source.fromInputStream( stream ).getLines
    val top = lines.take(400) mkString ","
    val scn = scenario("Test Mass Users")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401))
        .check(headerRegex("Set-Cookie", "CSRF-TOKEN=(.*); [P,p]ath=/").saveAs("csrf_token")))
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authentication")
        .headers(headers_http_authenticated)
        .formParam("j_username", "admin")
        .formParam("j_password", "vktrgt333333")
        .formParam("remember-me", "true")
        .formParam("submit", "Login"))
        .pause(1)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200))
        .check(headerRegex("Set-Cookie", "CSRF-TOKEN=(.*); [P,p]ath=/").saveAs("csrf_token")))
        .pause(10)
        .exec(http("Find leaders")
        .post("/api/users/leaders")
        .headers(headers_http_authenticated_undf)
        .formParam("users", top)
        .formParam("min", 2)
        .formParam("taskInfo", ""+(System.currentTimeMillis / 1000))
        .formUpload("file", "users.txt")
        .asMultipartForm
        .check(status.is(200)))

    val users = scenario("VKUserServiceImpl").exec(scn)

    setUp(
        users.inject(rampUsers(15) over (5 minutes))
    ).protocols(httpConf)


}
