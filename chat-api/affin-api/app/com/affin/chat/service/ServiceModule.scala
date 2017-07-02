package com.affin.chat.service

import com.affin.chat.service.provider._
import net.codingwell.scalaguice.ScalaModule

class ServiceModule extends ScalaModule {

  override def configure(): Unit = {
    bind[QueueService].to[RabbitMQService]
  }
}
