package com.affin.chat.util

import java.net.URL
import javax.mail.internet.InternetAddress

import com.wix.accord.NullSafeValidator
import com.wix.accord.ViolationBuilder._

import scala.util.Try

object Valid {

  object Email extends NullSafeValidator[String](
    v => Try(new InternetAddress(v).validate()).isSuccess,
    _ -> "is not a Email"
  )

  object Url extends NullSafeValidator[String](
    v => Try(new URL(v)).isSuccess,
    _ -> "is not a Url"
  )

  object Metadata extends NullSafeValidator[Map[String, String]](
    v => v.size <= 20 && v.forall { case (k, v) => k.length <= 40 && v.length <= 512 },
    _ -> "invalid metadata"
  )

  object OnlyNumber extends NullSafeValidator[String](
    v => v.matches("[0-9]+"),
    _ -> "does not has only number"
  )

}