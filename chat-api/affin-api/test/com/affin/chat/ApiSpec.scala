package com.affin.chat

import akka.util.Timeout
import com.google.inject.testing.fieldbinder.BoundFieldModule
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, Inspectors, Matchers, OptionValues, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.{HeaderNames, HttpProtocol, HttpVerbs, MimeTypes, Status, Writeable}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.mvc._
import play.api.test._

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

class Spec extends WordSpec
  with Matchers with Inspectors
  with ScalaFutures with OptionValues
  with MockitoSugar {
  implicit protected val ec: ExecutionContextExecutor = ExecutionContext.global
}

trait AppSpec extends Spec
  with GuiceOneAppPerSuite with BeforeAndAfterEach {

  private val integrationTestModule = BoundFieldModule.of(this)

  protected final def inject[T: ClassTag]: T = {
    app.injector.instanceOf[T]
  }

  override def fakeApplication() = {
    build(new GuiceApplicationBuilder()).build()
  }

  protected def build(builder: GuiceApplicationBuilder): GuiceApplicationBuilder = {
    builder
      .overrides(integrationTestModule)
  }
}

sealed trait ApiHelpers extends Results
  with HeaderNames with HttpVerbs with Status
  with MimeTypes with HttpProtocol with DefaultAwaitTimeout
  with Writeables with EssentialActionCaller with RouteInvokers
  with FutureAwaits

class ApiSpec extends AppSpec with ApiHelpers {

  protected val Request = FakeRequest

  implicit class RickFakeRequest[A](request: FakeRequest[A]) {
    def withInput[T](value: T): FakeRequest[T] = {
      request.withBody(value)
    }

    def withInputJson[T: Writes](value: T): FakeRequest[AnyContentAsJson] = {
      request.withJsonBody(Json.toJson(value))
    }

    def run(implicit w: Writeable[A]): Future[Result] = {
      route(app, request)(w).get
    }
  }

  implicit class RickResultExtractors(of: Future[Result]) {
    def contentType(implicit timeout: Timeout = Helpers.defaultAwaitTimeout) = Helpers.contentType(of)(timeout)

    def charset(implicit timeout: Timeout = Helpers.defaultAwaitTimeout) = Helpers.charset(of)(timeout)

    def contentAsString(implicit timeout: Timeout = Helpers.defaultAwaitTimeout) = Helpers.contentAsBytes(of)(timeout)

    def contentAsBytes(implicit timeout: Timeout = Helpers.defaultAwaitTimeout) = Helpers.contentAsBytes(of)(timeout)

    def contentAsJson(implicit timeout: Timeout = Helpers.defaultAwaitTimeout) = Helpers.contentAsJson(of)(timeout)

    def status(implicit timeout: Timeout = Helpers.defaultAwaitTimeout) = Helpers.status(of)(timeout)

    def cookies(implicit timeout: Timeout = Helpers.defaultAwaitTimeout) = Helpers.cookies(of)(timeout)

    def flash(implicit timeout: Timeout = Helpers.defaultAwaitTimeout) = Helpers.flash(of)(timeout)

    def session(implicit timeout: Timeout = Helpers.defaultAwaitTimeout) = Helpers.session(of)(timeout)

    def redirectLocation(implicit timeout: Timeout = Helpers.defaultAwaitTimeout) = Helpers.redirectLocation(of)(timeout)

    def header(header: String)(implicit timeout: Timeout = Helpers.defaultAwaitTimeout): Option[String] = Helpers.header(header, of)(timeout)

    def headers(implicit timeout: Timeout = Helpers.defaultAwaitTimeout): Map[String, String] = Helpers.headers(of)(timeout)

    def contentAs[T: Reads](implicit timeout: Timeout = Helpers.defaultAwaitTimeout): T = contentAsJson().as[T]
  }
}
