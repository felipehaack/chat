package com.affin.chat.model

import com.affin.chat.util.Valid
import com.wix.accord.dsl._

@json case class Email(
                        origin: String,
                        destination: String
                      )

object Email {

  implicit val Validator = validator[Email] { v =>
    v.origin is Valid.Email
    v.destination is Valid.Email
  }
}
