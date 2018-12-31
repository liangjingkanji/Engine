/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.log;

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
