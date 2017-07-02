package com.affin.chat

import com.affin.chat.config.{ConfigModule, RabbitMQ}
import com.affin.chat.service.ServiceModule
import com.google.inject.Provides
import com.rabbitmq.client.ConnectionFactory
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import net.codingwell.scalaguice.ScalaModule
import play.api.Configuration

class AffinModule extends ScalaModule {

  def configure(): Unit = {
    install(new ConfigModule)
    install(new ServiceModule)
  }

  @Provides
  def configRabbitMQModule(config: Configuration): ConnectionFactory = {
    val rabbitmq = config.underlying.as[RabbitMQ]("rabbitmq")
    val factory = new ConnectionFactory()
    factory.setHost(rabbitmq.host)
    factory.setPort(rabbitmq.port)
    factory.setPassword(rabbitmq.password)
    factory.setUsername(rabbitmq.username)
    factory
  }
}
