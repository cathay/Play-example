package com.emobi.controllers

import com.emobi.service.MGageService
import com.emobi.utils.SubscriptionsWSTestData._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.specs2.mock._
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSResponse
import play.api.mvc._
import play.api.test.Helpers.{GET => GET_REQUEST, _}
import play.api.test.{FakeRequest, WithApplication}

import scala.concurrent.Future

class MGageSubscriptionsControllerFunctionalSpec extends PlaySpec
  with GuiceOneAppPerSuite with Results with Mockito {

  import scala.concurrent.ExecutionContext.Implicits.global

  "Subscription Controller" should {

    val appWithServer = createApplication(200)
    val appWithTimeoutServer = createApplication(REQUEST_TIMEOUT)
    val appWithInternalErrorServer = createApplication(INTERNAL_SERVER_ERROR)

    "get status properly when the service is in a good mod" in new WithApplication(app = appWithServer) {

      val controller = app.injector.instanceOf[MGageSubscriptionsController]
      val result = controller.getStatus(1)(FakeRequest())

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsJson(result).toString() mustBe """{"mig_sid":1,"status":"active"}"""
    }

    "return proper message for gateway timeout exception" in new WithApplication(app = appWithTimeoutServer) {

      val controller = app.injector.instanceOf[MGageSubscriptionsController]
      val result = controller.getStatus(1)(FakeRequest())

      status(result) mustBe REQUEST_TIMEOUT
      contentAsJson(result).toString() mustBe """{"message":"Timeout to payment gateway"}"""
    }

    "return proper message for gateway internal exception" in  new WithApplication(app = appWithInternalErrorServer) {

      val controller = app.injector.instanceOf[MGageSubscriptionsController]
      val result = controller.getStatus(1)(FakeRequest())

      status(result) mustBe INTERNAL_SERVER_ERROR
      contentAsJson(result).toString() mustBe """{"message":"Gateway internal error"}"""
    }
  }

  def createApplication(status: Int) = {

    val mockService = mock[MGageService]
    val result = mock[WSResponse]
    result.json returns createMGageResponse()
    result.status returns status

    mockService.getSubscriptionStatus(1) returns Future(result)
    GuiceApplicationBuilder().overrides(bind[MGageService].toInstance(mockService)).build()

  }
}
