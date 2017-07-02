package com.affin.chat.exception

import com.affin.chat.model.{Error, TypedId}

import scala.reflect.ClassTag
import scala.util.control.NoStackTrace

class AffinChatException(val message: String) extends RuntimeException(message)

object AffinChatException {

  def systemError(desc: String) = throw new AffinChatException(desc)

  def notFound[T: ClassTag] = throw new NotFoundFailure(getSimpleName[T])

  def updateFail[T: ClassTag] = throw new UpdateFailure(getSimpleName[T])

  def invalidId[T <: TypedId : ClassTag] = throw new InvalidIdFailure(getSimpleName[T])

  def invalidInput(desc: String) = throw new InvalidInputFailure(desc)

  def invalidInputs(errors: Set[Error]) = throw new InvalidInputsFailure(errors)

  private def getSimpleName[T](implicit ct: ClassTag[T]): String = {
    ct.runtimeClass.getSimpleName
  }
}

class AffinChatFailure(val code: String) extends AffinChatException(code) with NoStackTrace

class NotFoundFailure(name: String) extends AffinChatFailure(s"notFound.${name}")

class InvalidFailure(name: String) extends AffinChatFailure(s"invalid.${name}")

class InvalidIdFailure(name: String) extends InvalidFailure(s"id.${name}")

class InvalidInputFailure(name: String) extends InvalidFailure(name)

class InvalidInputsFailure(val errors: Set[Error]) extends InvalidFailure("input")

class UpdateFailure(val name: String) extends InvalidFailure(s"update.$name")
