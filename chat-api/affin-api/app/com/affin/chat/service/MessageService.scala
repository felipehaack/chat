package com.affin.chat.service

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}

import com.affin.chat.model.Chat
import com.affin.chat.service.provider.QueueService
import com.affin.chat.util.Validator
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MessageService @Inject()(
                                queueService: QueueService
                              )(
                                implicit private val ec: ExecutionContext
                              ) {

  def create(
              input: Chat
            ): Future[Chat] = {

    Validator.input(input)

    val default = applyDefault(input)

    queueService.delivery(input.email.destination, default.toBytes)
      .map(_ => default)
  }

  def getAll(
              input: Chat.List
            ): Future[List[Chat]] = {

    Validator.input(input)

    for {
      messages <- queueService.retrieve(input.email)
      chats = messages.flatMap { message =>
        val json = new String(message)
        Json.fromJson[Chat](Json.parse(json)).asOpt
      }
    } yield chats
  }

  private def applyDefault(
                            chat: Chat
                          ): Chat = {

    chat.copy(created = Some(LocalDateTime.now()))
  }
}
