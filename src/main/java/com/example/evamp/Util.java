package com.example.evamp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
  public static String formatDate(String date) throws ParseException {
    if (date.isEmpty()) {
      return "";
    }
    if (date.length()==5){
      date = "0"+date;
    }

    final String OLD_FORMAT = "ddMMyy";
    final String NEW_FORMAT = "yyyy-MM-dd";

    SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
    Date d = sdf.parse(date);
    sdf.applyPattern(NEW_FORMAT);
    return sdf.format(d);
  }

  public static void printJson(RequestJson requestJson) {
    Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().setPrettyPrinting().create();
    String prettyJson = gson.toJson(requestJson);
    System.out.println(prettyJson);
  }

  public static String mapAction(String action) {
    if (action.equalsIgnoreCase("add")){
      return "hire";
    }
    else if (action.equalsIgnoreCase("update")){
      return "change";
    }
    else if (action.equalsIgnoreCase("delete")){
      return "terminate";
    }
    return "";
  }
}
