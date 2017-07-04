package com.affin.chat.connection

import javax.inject.{Inject, Singleton}

import com.rabbitmq.client.{Channel, ConnectionFactory}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class QueueConnection @Inject()(
                                 connectionFactory: ConnectionFactory
                               )(
                                 implicit private val ec: ExecutionContext
                               ) {

  private var connection = connectionFactory.newConnection()
  private var channel = connection.createChannel()

  private def defineConnection() = {

    if (!connection.isOpen) {
      connection = connectionFactory.newConnection()
      channel = connection.createChannel()
    }
  }

  private def defineChannel() = {

    if (!channel.isOpen)
      channel = connection.createChannel()
  }

  def withAsyncTransaction[A](
                               block: Channel => Future[A]
                             ): Future[A] = {

    defineConnection()
    defineChannel()

    block(channel)
  }
}
