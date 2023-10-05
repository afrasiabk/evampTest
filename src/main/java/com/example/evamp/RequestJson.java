package com.example.evamp;

import java.util.ArrayList;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@lombok.Data
@AllArgsConstructor
@Builder
public class RequestJson {

  private String uuid;
  private String fname;
  private ArrayList<String> errors;
  private ArrayList<Payload> payload;

}
@lombok.Data
@AllArgsConstructor
class Payload {
  private String employeeCode;
  private String action;
  private Map data;
  private ArrayList<PayComponent> payComponents;
}

@lombok.Data
@AllArgsConstructor
class PayComponent {

  private Double amount;
  private String type;
  private String currency;
  private String startDate;
  private String endDate;
}


