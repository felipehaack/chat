package com.affin.chat.config

import com.google.inject.Provides
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import net.codingwell.scalaguice.ScalaModule
import play.api.Configuration

class ConfigModule extends ScalaModule {

  def configure(): Unit = {
  }

  @Provides
  def configChatModule(config: Configuration): ChatConfig = {
    config.underlying.as[ChatConfig]("chat")
  }
}
