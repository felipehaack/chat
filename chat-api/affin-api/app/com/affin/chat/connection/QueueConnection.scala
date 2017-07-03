package com.affin.chat.connection

import javax.inject.{Inject, Singleton}

import com.affin.chat.exception.AffinChatException
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
      _ <- Future {
        connection.isOpen match {
          case false => throw AffinChatException.systemError("connection.queue.lost")
          case true =>
        }
      }
      result <- block(channel)
    } yield result
  }
}
