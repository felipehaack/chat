package com.affin.chat.util

import java.time.LocalDate
import java.time.temporal.ChronoUnit

import com.affin.chat.exception.AffinChatException
import com.affin.chat.model.Error
import com.wix.accord._

import scala.util.matching.Regex

object Validator {

  def input[T: Validator](value: T): Unit = {
    validate(value) match {
      case Success =>
      case Failure(violations) =>
        val errors = violations.map { v =>
          val code = Descriptions.render(v.description)
          Error(code, s"${v.value} ${v.constraint}")
        }
        AffinChatException.invalidInputs(errors)
    }
  }

  def enum(enum: Enumeration, value: String, name: String): Unit = {
    enum.values.find(_.toString == value).getOrElse {
      AffinChatException.invalidInput(name + ".enum")
    }
  }

  def nonEmpty(value: String, name: String = "param"): Unit = {
    if (value.isEmpty) {
      AffinChatException.invalidInput(name + ".empty")
    }
  }

  def max(value: String, size: Int, name: String = "param"): Unit = {
    if (value.length > size) {
      AffinChatException.invalidInput(name + ".max")
    }
  }

  def min(value: String, size: Int, name: String = "param"): Unit = {
    if (value.length < size) {
      AffinChatException.invalidInput(name + ".min")
    }
  }

  def sizeBetween(value: String, min: Int, max: Int, name: String = "param"): Unit = {
    this.nonEmpty(value, name)
    this.min(value, min, name)
    this.max(value, max, name)
  }

  def size(value: String, size: Int, name: String = "param"): Unit = {
    if (value.length != size) {
      AffinChatException.invalidInput(s"name.size")
    }
  }

  def pattern(value: String, regex: Regex, name: String = "param"): Unit = {
    if (regex.findFirstIn(value).isEmpty) {
      AffinChatException.invalidInput(s"name.pattern")
    }
  }

  def maxDaysBetween(from: LocalDate, to: LocalDate, maxDays: Long, name: String) = {
    if (ChronoUnit.DAYS.between(from, to) > maxDays)
      AffinChatException.invalidInput(s"$name.range")
  }

  def dateOlderThan(oldest: LocalDate, date: LocalDate, name: String) = {
    if (date.isBefore(oldest))
      AffinChatException.invalidInput(s"$oldest.old")
  }

}