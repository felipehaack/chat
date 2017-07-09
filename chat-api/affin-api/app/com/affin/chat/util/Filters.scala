package com.affin.chat.util

import java.sql.SQLException
import javax.inject.{Inject, Singleton}

import akka.stream.Materializer
import com.affin.chat.exception._
import com.affin.chat.model.Error
import net.logstash.logback.marker.Markers._
import play.api.http.HttpEntity
import play.api.http.Status._
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.Results.{Status, _}
import play.api.mvc.{Filter, RequestHeader, ResponseHeader, Result}

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LoggingFilter @Inject()(
                               implicit val mat: Materializer,
                               implicit val ec: ExecutionContext
                             ) extends Filter with Logging {

  private val pingPath = "/ping"
  private val responseError = "responseError"

  private val excludeHeaders = List(
    "Authorization"
  )

  implicit class RichResult(result: Result) {
    def enableCors = result.withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Access-Control-Allow-Methods" -> "OPTIONS, GET, POST, PUT, DELETE, HEAD",
      "Access-Control-Allow-Headers" -> "Accept, Content-Type, Origin, X-Json, X-Prototype-Version, X-Requested-With",
      "Access-Control-Allow-Credentials" -> "true"
    )

    def enableCorsPreFlight = result.withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Access-Control-Expose-Headers" -> "WWW-Authenticate, Server-Authorization",
      "Access-Control-Allow-Methods" -> "POST, GET, OPTIONS, PUT, DELETE",
      "Access-Control-Allow-Headers" -> "x-requested-with,content-type,Cache-Control,Pragma,Date"
    )
  }

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    val startTime = System.currentTimeMillis

    requestHeader.method match {
      case "OPTIONS" =>
        val endTime = System.currentTimeMillis
        val requestTime = endTime - startTime
        val header = ResponseHeader(OK)
        val body = HttpEntity.NoEntity
        logger.info(s"${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms")
        Future(Result(header, body).enableCorsPreFlight)
      case _ =>
        nextFilter(requestHeader).map { result =>
          val endTime = System.currentTimeMillis
          val requestTime = endTime - startTime
          if (requestHeader.path != pingPath) {
            val fields = mutable.Map[String, Any](
              "requestUri" -> requestHeader.uri,
              "requestMethod" -> requestHeader.method,
              "requestTime" -> requestTime,
              "responseStatus" -> result.header.status
            )

            requestHeader.headers.remove(excludeHeaders: _*).headers.foreach(fields.+=)

            result.header.status match {
              case BAD_REQUEST =>
                result.body.consumeData.foreach(b => fields.+=(responseError -> b.utf8String))
              case _ =>
            }
            logger.info(appendEntries(fields), s"${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms returned ${result.header.status}")
          }
          result.withHeaders("Request-Time" -> requestTime.toString).enableCors
        }
    }
  }
}


@Singleton
class FailureFilter @Inject()(val messagesApi: MessagesApi,
                              implicit val mat: Materializer,
                              implicit val ec: ExecutionContext
                             ) extends Filter with I18nSupport with Logging {

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    nextFilter(requestHeader)
      .recover {
        case ex: InvalidInputFailure =>
          FailureResult(BadRequest, ex.code)
        case ex: InvalidInputsFailure =>
          FailureResult(BadRequest, ex.code, Some(ex.errors))
        case ex: InvalidFailure =>
          FailureResult(BadRequest, ex.code)
        case ex: NotFoundFailure =>
          FailureResult(NotFound, ex.code)
        case ex: SQLException if ex.getSQLState == "23505" =>
          FailureResult(Conflict)
        case ex: SQLException =>
          logger.error(s"Failure:$requestHeader - ${ex.getSQLState}", ex)
          FailureResult(InternalServerError, "server.internal.error")
      }
  }

  private def FailureResult(status: Status, code: String = "", errors: Option[Set[Error]] = None): Result = {
    if (code.nonEmpty) {
      val error = Error(code, Messages(code), errors)
      status(Json.toJson(error))
    } else {
      status
    }
  }
}