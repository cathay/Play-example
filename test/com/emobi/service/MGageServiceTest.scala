package com.emobi.service

import com.typesafe.config.Config
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.libs.json.JsObject
import play.api.mvc._
import play.api.routing.sird._
import play.api.test._
import play.core.server.Server

import scala.concurrent.Await
import scala.concurrent.duration._
import com.emobi.utils.SubscriptionsWSTestData._

class MGageServiceTest extends Specification with Mockito {

  //TODO This test is set up different way with controller tests. However, I think we can use the same WithServer trait to complete it. Consider to refactor???
  import com.emobi.service.MGageService._
  import scala.concurrent.ExecutionContext.Implicits.global

  "MGageService" should {
    "get proper status when payment gateway works fine" in {

      withMGageService(service => {
        val result = Await.result(service.getSubscriptionStatus(1), 10.seconds)
        result.json must_== createMGageResponse
      }, createMGageResponse)
    }
  }

  def withMGageService[T](block: MGageService => T, testData: JsObject): T = {

    Server.withRouterFromComponents() { components =>
      import Results._
      import components.{ defaultActionBuilder => Action }
    {
      case POST(p"/subscriptions/1/status") => Action {
        Ok(testData)
      }
    }
    } { implicit port =>
      WsTestClient.withClient { client =>
        block(new MGageService(client, createMockConfig()))
      }
    }
  }

  def createMockConfig() = {
    val config = mock[Config]
    config.getString(URL_CONFIG) returns ""
    config.getString(API_KEY_CONFIG) returns ""
    config.getInt(API_TIME_OUT) returns 0
    config
  }
}