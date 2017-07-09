package com.affin.chat

import java.net.URI

import com.affin.chat.config.{ConfigModule, RabbitMQConfig}
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
    val rabbitmq = config.underlying.as[RabbitMQConfig]("rabbitmq")
    val factory = new ConnectionFactory()
    val uri = new URI(rabbitmq.url)
    factory.setUri(uri)
    factory
  }
}
