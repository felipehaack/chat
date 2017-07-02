package com.affin.chat.service.provider

import javax.inject.{Inject, Singleton}

import com.affin.chat.connection.QueueConnection
import com.affin.chat.exception.AffinChatException

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RabbitMQService @Inject()(
                                 queueConnection: QueueConnection
                               )(
                                 implicit private val ec: ExecutionContext
                               ) extends QueueService {

  def createQueue(
                   queue: String
                 ): Future[Boolean] = {

    queueConnection.withAsyncTransaction { implicit channel =>
      for {
        _ <- Future(channel.queueDeclare(queue, false, false, false, null))
      } yield true
    }
  }

  def delivery(
                queue: String,
                data: Array[Byte]
              ): Future[Boolean] = {

    queueConnection.withAsyncTransaction { implicit channel =>
      for {
        _ <- createQueue(queue)
        _ <- Future(channel.basicPublish("", queue, null, data))
      } yield true
    }
  }

  def retrieve(
                queue: String
              ): Future[List[Array[Byte]]] = {

    queueConnection.withAsyncTransaction { implicit channel =>
      for {
        _ <- createQueue(queue)
        _ <- Future(channel.queueDeclarePassive(queue))
        messages <- Future.sequence {
          (0L until channel.messageCount(queue)).map { _ =>
            Future {
              Option(channel.basicGet(queue, false)) match {
                case Some(response) =>
                  val tag = response.getEnvelope.getDeliveryTag
                  channel.basicAck(tag, true)
                  response.getBody
                case None => AffinChatException.systemError(s"queue.not_found.$queue")
              }
            }
          }
        }
      } yield messages.toList
    }
  }
}
