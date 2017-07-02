package api

import javax.inject.{Inject, Singleton}

import com.affin.chat.model.Chat
import com.affin.chat.service.MessageService

@Singleton
class MessageApi @Inject()(
                            messageService: MessageService
                          ) extends Api {

  def delivery(
              ) = Action.async(json[Chat]) { implicit request =>

    val input = request.body

    val result = messageService.delivery(input)

    Ok.asJson(result)
  }

  def retrieve(
                email: String
              ) = Action.async { implicit request =>

    val input = Chat.List(email)

    val result = messageService.retrieve(input)

    Ok.asJson(result)
  }
}
