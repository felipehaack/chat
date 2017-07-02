package com.affin.chat.service.provider

import com.affin.chat.service.Service

import scala.concurrent.Future

trait QueueService extends Service {

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
