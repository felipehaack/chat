package com.affin.chat.service.provider

import scala.concurrent.Future

trait RabbitMQ {

  def createQueue(
                   queue: String
                 ): Future[Boolean]

  def delivery(
                queue: String,
                data: Array[Byte]
              ): Future[Boolean]

  def retrieve(
                queue: String
              ): Future[List[Array[Byte]]]
}
