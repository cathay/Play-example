package com.emobi.controllers

import javax.inject.Inject

import com.emobi.service.MGageService
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import play.api.mvc.{AbstractController, ControllerComponents}


class MGageSubscriptionsController @Inject()(
                                              val cc: ControllerComponents,
                                              mGageService: MGageService
                                            )
  extends AbstractController(cc)  {

  //TODO Investigate which one should be used
  //implicit lazy val executionContext = defaultExecutionContext
  import scala.concurrent.ExecutionContext.Implicits.global

  def getStatus(id: Long) = Action.async {
    val result = mGageService.getSubscriptionStatus(id)
    result.map(r => {
      //TODO Make an log here for the result
      r.status match {

        case 200 => {
          val response = r.json \ "response"
          Ok(response.get)
        }

        case 404 => new Status(r.status)(notFoundResponse)
        case 408 => new Status(r.status)(timeoutResponse)
        case x if x >= 400 && x < 500 => new Status(r.status)(tailorWSErrorResponse(r))
        case x if x >= 500 => new Status(r.status)(gatewayError)
        case _ => new Status(r.status)(tailorWSErrorResponse(r))
      }
    })
  }

  //TODO Extract message to use play.i18n
  def gatewayError() = {
    Json.obj(
      "message" -> "Gateway internal error"
    )
  }

  def notFoundResponse() = {
    Json.obj(
      "message" -> "Invalid subscription or invalid gateway url"
    )
  }

  def timeoutResponse() = {
    Json.obj(
      "message" -> "Timeout to payment gateway"
    )
  }

  def tailorWSErrorResponse(response: WSResponse) = {
    Json.obj(
      "message" -> response.statusText
    )
  }
}
