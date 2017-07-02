package com.affin.chat.service

import com.affin.chat.exception.AffinChatException
import com.affin.chat.util.Logging

import scala.reflect.ClassTag

trait Service extends Logging {

  implicit class RichOption[T: ClassTag](value: Option[T]) {
    def getOrNotFound = {
      value.getOrElse(AffinChatException.notFound[T])
    }
  }

}