package com.affin.chat.connection

import javax.inject.{Inject, Singleton}

import com.rabbitmq.client.{Channel, ConnectionFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class QueueConnection @Inject()(
                                 connectionFactory: ConnectionFactory
                               )(
                                 implicit private val ec: ExecutionContext
                               ) {

  private val connection = connectionFactory.newConnection()
  private val channel = connection.createChannel()

  def withAsyncTransaction[A](
                               block: Channel => Future[A]
                             ): Future[A] = {
    for {
      result <- block(channel)
    } yield result
  }
}
