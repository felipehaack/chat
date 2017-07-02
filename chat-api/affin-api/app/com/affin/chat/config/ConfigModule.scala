package com.affin.chat.config

import com.google.inject.Provides
import com.rabbitmq.client.ConnectionFactory
import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import net.codingwell.scalaguice.ScalaModule

import scala.concurrent.ExecutionContext

class ConfigModule extends ScalaModule {

  def configure(): Unit = {
    bind[ExecutionContext].toInstance(ExecutionContext.global)
  }

  @Provides
  def configChatModule(config: Config): ChatConfig = {
    config.as[ChatConfig]("chat")
  }

  @Provides
  def configRabbitMQModule(config: Config): ConnectionFactory = {
    val rabbitmq = config.as[RabbitMQ]("rabbitqm")
    val factory = new ConnectionFactory()
    factory.setHost(rabbitmq.host)
    factory.setPort(rabbitmq.port)
    factory.setPassword(rabbitmq.password)
    factory.setUsername(rabbitmq.username)
    factory
  }
}
