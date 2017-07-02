package com.affin.chat.service

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}

import com.affin.chat.model.Chat
import com.affin.chat.service.provider.RabbitMQProvider
import com.affin.chat.util.Validator
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MessageService @Inject()(
                                rabbitMQProvider: RabbitMQProvider
                              )(
                                implicit private val ec: ExecutionContext
                              ) {

  def delivery(
                input: Chat
              ): Future[Boolean] = {

    Validator.input(input)

    val default = applyDefault(input)

    rabbitMQProvider.delivery(input.email.destination, default.toBytes)
  }

  def retrieve(
                input: Chat.List
              ): Future[List[Chat]] = {

    Validator.input(input)

    for {
      messages <- rabbitMQProvider.retrieve(input.email)
      chats = messages.map { message =>
        val json = new String(message)
        val model = Json.fromJson[Chat](Json.parse(json)).get
        model
      }
    } yield chats
  }

  private def applyDefault(
                            chat: Chat
                          ): Chat = {

    chat.copy(created = Some(LocalDateTime.now()))
  }
}
