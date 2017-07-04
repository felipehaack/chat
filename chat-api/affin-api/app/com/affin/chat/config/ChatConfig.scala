package com.affin.chat.config

case class ChatConfig(
                       rabbitmq: RabbitMQ
                     )

case class RabbitMQ(
                     url: String
                   )