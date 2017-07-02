package com.affin.chat.connection

import javax.inject.{Inject, Singleton}

import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class QueueConnection @Inject()(
                                 connectionFactory: ConnectionFactory
                               )(
                                 implicit private val ec: ExecutionContext
                               ) {
  private def createConnection(
                              ): Future[Connection] = Future {
    connectionFactory.newConnection()
  }

  private def createChannel(
                             connection: Connection
                           ): Future[Channel] = Future {
    connection.createChannel()
  }

  def withAsyncTransaction[A](
                               block: Channel => Future[A]
                             ): Future[A] = {

    for {
      connection <- createConnection()
      channel <- createChannel(connection)
      result <- block(channel)
      _ = channel.close()
      _ = connection.close()
    } yield result
  }
}
