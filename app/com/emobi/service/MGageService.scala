package com.emobi.service

import com.google.inject.Inject
import com.typesafe.config.Config
import play.api.libs.ws.{EmptyBody, WSClient}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class MGageService @Inject()(
                              ws: WSClient,
                              configuration: Config
                            ) {

  import MGageService._

  lazy val mGageUrl = configuration.getString(URL_CONFIG)
  lazy val apiKey = configuration.getString(API_KEY_CONFIG)
  lazy val mGageTimeout = configuration.getInt(API_TIME_OUT)

  //TODO Consider to inject executionContext to constructor
  def getSubscriptionStatus(id: Long)(implicit executionContext: ExecutionContext) = {

    val url  = s"$mGageUrl/subscriptions/$id/status"
    ws.url(url)
      .addHttpHeaders(
        "Accept" -> "application/json",
        "MigPay-API-Key" -> apiKey
      ).withRequestTimeout(mGageTimeout.millis)
      .post(EmptyBody)
  }
}

object MGageService {
  val URL_CONFIG = "com.emobi.gateway.mgage.url"
  val API_KEY_CONFIG = "com.emobi.gateway.mgage.apiKey"
  val API_TIME_OUT = "com.emobi.gateway.timeout"
}
