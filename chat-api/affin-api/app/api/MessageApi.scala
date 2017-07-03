package api

import javax.inject.{Inject, Singleton}

import com.affin.chat.model.Chat
import com.affin.chat.service.MessageService

@Singleton
class MessageApi @Inject()(
                            messageService: MessageService
                          ) extends Api {

  def create(
            ) = Action.async(json[Chat]) { implicit request =>

    val input = request.body

    val result = messageService.create(input)

    Ok.asJson(result)
  }

  def getAll(
              email: String
            ) = Action.async { implicit request =>

    val input = Chat.List(email)

    val result = messageService.getAll(input)

    Ok.asJson(result)
  }
}
