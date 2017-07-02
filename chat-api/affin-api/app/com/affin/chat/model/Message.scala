package com.affin.chat.model

import com.wix.accord.dsl._

@jsonstrict case class Message(
                                text: Option[String]
                              )

object Message {

  implicit val Validator = validator[Message] { v =>
    v.text.each has size.between(1, 1024)
  }
}
