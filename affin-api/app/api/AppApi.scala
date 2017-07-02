package api

import javax.inject.Singleton

import com.affin.chat.Version

@Singleton
class AppApi extends Api {

  def currentVersion = Action {
    Ok(Version.current)
  }

  def ping = Action {
    Ok
  }
}
