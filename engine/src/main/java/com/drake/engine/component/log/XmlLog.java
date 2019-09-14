/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.log;

import android.util.Log;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


public class XmlLog {

  public static void printXml(String tag, String xml, String headString) {
    if (xml != null) {
      xml = XmlLog.formatXML(xml);
      xml = headString + "\n" + xml;
    } else {
      xml = headString + LogCat.NULL_TIPS;
    }
    LogCatHelper.printLine(tag, true);
    String[] lines = xml.split(LogCat.LINE_SEPARATOR);
    for (String line : lines) {
      if (!LogCatHelper.isEmpty(line)) {
        Log.d(tag, "║ " + line);
      }
    }
    LogCatHelper.printLine(tag, false);
  }

  private static String formatXML(String inputXML) {
    try {
      Source xmlInput = new StreamSource(new StringReader(inputXML));
      StreamResult xmlOutput = new StreamResult(new StringWriter());
      Transformer transformer = TransformerFactory.newInstance()
              .newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.transform(xmlInput, xmlOutput);
      return xmlOutput.getWriter()
              .toString()
              .replaceFirst(">", ">\n");
    } catch (Exception e) {
      e.printStackTrace();
      return inputXML;
    }
  }

}
