package com.emobi.utils

import play.api.libs.json.Json

/**
  * Created by daicarum on 10/12/17.
  */
object SubscriptionsWSTestData {

  //TODO Need to extract as json file
  def createMGageResponse() = {
    Json.obj("response" -> Json.obj(
      "mig_sid" -> 1,
      "status" -> "active"
    ))
  }
}
