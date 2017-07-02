package com.affin.chat.model

import java.time.LocalDateTime

import com.affin.chat.util.Valid
import com.wix.accord.dsl._
import play.api.libs.json.Json

@json case class Chat(
                       email: Email,
                       message: Message,
                       created: Option[LocalDateTime]
                     )

object Chat {

  implicit class RichToBytes(chat: Chat) {
    def toBytes = Json.toJson(chat).toString().getBytes()
  }

  implicit val Validator = validator[Chat] { v =>
    v.email is valid
    v.message is valid
  }

  @jsonstrict case class List(
                               email: String
                             )

  object List {

    implicit val Validator = validator[List] { r =>
      r.email is Valid.Email
    }
  }

}