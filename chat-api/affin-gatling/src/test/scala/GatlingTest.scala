import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._

class GatlingTest extends Simulation {

  lazy val createUser = 1000
  lazy val createUserMessage = 5

  lazy val getUser = 1000
  lazy val getUserMessage = 10

  lazy val httpConf = http
    .baseURL("http://localhost:9000/v1")

  lazy val feeder = csv("message.csv").random

  lazy val create = scenario("Send Message")
    .feed(feeder)
    .repeat(createUserMessage, "n") {
      exec(http("create")
        .post("/message")
        .header("Content-Type", "application/json")
        body (StringBody( """{"email":{"origin":"${origin}","destination":"${destination}"},"message":{"text":"${message}"}}"""))
      )
    }

  lazy val get = scenario("Get Messages")
    .feed(feeder)
    .repeat(getUserMessage, "n") {
      exec(http("getAll")
        .get("/message/${origin}")
      )
    }

  setUp(create.inject(atOnceUsers(createUser)), get.inject(atOnceUsers(getUser)).protocols(httpConf))
}