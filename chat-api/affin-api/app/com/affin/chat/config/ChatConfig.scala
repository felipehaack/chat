package com.affin.chat.config

case class ChatConfig(
                       rabbitmq: RabbitMQ
                     )

case class RabbitMQ(
                     host: String,
                     port: Int,
                     username: String,
                     password: String
                   )