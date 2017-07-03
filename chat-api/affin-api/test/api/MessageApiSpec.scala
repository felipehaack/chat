package api

import com.affin.chat.ApiSpec
import com.affin.chat.connection.QueueConnection
import com.affin.chat.model.Chat
import play.api.libs.json.{JsObject, Json}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class MessageApiSpec extends ApiSpec {

  val path = "/v1/message"

  val duration = Duration("10s")

  val connection = inject[QueueConnection]

  val queueOrigin = "origin@origin.com"
  val queueDestination = "destination@destination.com"

  val emailInput = Json.obj(
    "origin" -> queueOrigin,
    "destination" -> queueDestination
  )

  val messageInput = Json.obj(
    "text" -> "text message"
  )

  val chatInput = Json.obj(
    "email" -> emailInput,
    "message" -> messageInput
  )

  "Message Api" when {
    "validate" should {
      def invalidFields(input: JsObject) = {
        val request = Request(POST, s"$path").withInputJson(input)
        val response = request.run
        response.status shouldBe BAD_REQUEST
      }

      "return BAD_REQUEST when email origin is empty" in {
        val input = emailInput ++ Json.obj("origin" -> "")
        invalidFields(chatInput ++ Json.obj("email" -> input))
      }

      "return BAD_REQUEST when email origin is not valid" in {
        val input = emailInput ++ Json.obj("origin" -> "test@")
        invalidFields(chatInput ++ Json.obj("email" -> input))
      }

      "return BAD_REQUEST when email destination is empty" in {
        val input = emailInput ++ Json.obj("destination" -> "")
        invalidFields(chatInput ++ Json.obj("email" -> input))
      }

      "return BAD_REQUEST when email destinatio  is not valid" in {
        val input = emailInput ++ Json.obj("destination" -> "test@")
        invalidFields(chatInput ++ Json.obj("email" -> input))
      }

      "return BAD_REQUEST when message is more than 1024" in {
        val input = messageInput ++ Json.obj("text" -> List.fill(1025)("L").mkString)
        invalidFields(chatInput ++ Json.obj("message" -> input))
      }

      "return BAD_REQUEST when message is less than 1" in {
        val input = messageInput ++ Json.obj("text" -> "")
        invalidFields(chatInput ++ Json.obj("message" -> input))
      }
    }

    "create" should {
      "return OK with a chat" in {
        val request = Request(POST, s"$path").withInputJson(chatInput)

        val response = request.run
        response.status shouldBe OK

        val chat = response.contentAs[Chat]
        chat.created.isDefined shouldBe true

        removeQueue(queueOrigin, queueDestination)
      }
    }

    "getAll" should {
      "return OK with a list of chats" in {
        val request1 = Request(POST, s"$path").withInputJson(chatInput)
        Await.result(request1.run, duration)

        val request2 = Request(POST, s"$path").withInputJson(chatInput)
        Await.result(request2.run, duration)

        val request = Request(GET, s"$path/$queueDestination")

        val response = request.run
        response.status shouldBe OK

        val chat = response.contentAs[List[Chat]]
        chat.length should be.>=(2)

        removeQueue(queueOrigin, queueDestination)
      }
    }
  }

  private def removeQueue(queues: String*) = {
    val removing = connection.withAsyncTransaction { implicit channel =>
      Future {
        queues.foreach(channel.queueDelete)
      }
    }
    Await.result(removing, duration)
  }
}
