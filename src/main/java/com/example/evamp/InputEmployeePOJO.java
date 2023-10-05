package com.example.evamp;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InputEmployeePOJO {
  private String SystemId;
  private String ACTION;
  private String worker_name;
  private String worker_personalCode;
  private String worker_gender;
  private String worker_numberOfKids;
  private String worker_motherMaidenName;
  private String worker_grandmotherMaidenName;
  private String contract_signatureDate;
  private String contract_workStartDate;
  private String contract_type;
  private String contract_endDate;
  private String contract_workerId;
  private String pay_amount;
  private String pay_currency;
  private String pay_effectiveFrom;
  private String pay_effectiveTo;
  private String compensation_amount;
  private String compensation_type;
  private String compensation_currency;
  private String compensation_effectiveFrom;
  private String compensation_effectiveTo;


}
