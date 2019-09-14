/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.log;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonLog {

  public static void printJson(String tag, String msg, String headString) {
    String message;
    try {
      if (msg.startsWith("{")) {
        JSONObject jsonObject = new JSONObject(msg);
        message = jsonObject.toString(LogCat.JSON_INDENT);
      } else if (msg.startsWith("[")) {
        JSONArray jsonArray = new JSONArray(msg);
        message = jsonArray.toString(LogCat.JSON_INDENT);
      } else {
        message = msg;
      }
    } catch (JSONException e) {
      message = msg;
    }
    LogCatHelper.printLine(tag, true);
    message = headString + LogCat.LINE_SEPARATOR + message;
    String[] lines = message.split(LogCat.LINE_SEPARATOR);
    for (String line : lines) {
      Log.d(tag, "║ " + line);
    }
    LogCatHelper.printLine(tag, false);
  }
}
