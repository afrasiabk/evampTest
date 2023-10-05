package com.example.evamp;


import static com.example.evamp.Util.formatDate;
import static com.example.evamp.Util.printJson;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
  public void process() throws ParseException, IllegalAccessException {

    List<InputEmployeePOJO> list = mapFieldsFromCsv();

    RequestJson requestJson = RequestJson.builder()
        .uuid(UUID.randomUUID().toString())
        .fname("input_01.csv")
        .payload(getPayloadList(list))
        .build();

    printJson(requestJson);

  }

  private ArrayList<Payload> getPayloadList(List<InputEmployeePOJO> list)
      throws ParseException, IllegalAccessException {
    ArrayList<Payload> payloads = new ArrayList<>();
    for (InputEmployeePOJO e : list){

      PayComponent payComponent = new PayComponent(!e.getPay_amount().isEmpty()?Double.parseDouble(e.getPay_amount()):0,
          "Pay", e.getPay_currency(), formatDate(e.getPay_effectiveFrom()),formatDate(e.getPay_effectiveTo()));

      PayComponent compensationComponent = new PayComponent(!e.getCompensation_amount().isEmpty()?Double.parseDouble(e.getCompensation_amount()):0,
          !e.getCompensation_type().isEmpty()?"Compensation-"+e.getCompensation_type():"Compensation", e.getCompensation_currency(),
          formatDate(e.getCompensation_effectiveFrom()), formatDate(e.getCompensation_effectiveTo()));

      Payload payload = new Payload(e.getWorker_personalCode(), Util.mapAction(e.getACTION()), getData(e), new ArrayList<>(
          Arrays.asList(payComponent, compensationComponent)));

      payloads.add((payload));
    }
    discardIncomplete(payloads);
    return payloads;
  }

  private void discardIncomplete(ArrayList<Payload> payloads) {
    payloads.removeIf(p -> p.getEmployeeCode().isEmpty()
        || p.getAction().isEmpty()
        || p.getData().isEmpty()
        || p.getPayComponents().get(0).getAmount() == 0d
        || p.getPayComponents().get(0).getCurrency().isEmpty()
        || p.getPayComponents().get(0).getEndDate().isEmpty()
        || p.getPayComponents().get(0).getStartDate().isEmpty());

    payloads.removeIf(p -> p.getEmployeeCode().isEmpty()
        && p.getData().get("contract.workStartDate").toString().isEmpty());

  }

  private TreeMap<String, String> getData(InputEmployeePOJO pojo) throws IllegalAccessException, ParseException {
    TreeMap<String, String> map = new TreeMap<>();
    Field[] fs = pojo.getClass().getDeclaredFields();
    for(Field f : fs) {
      f.setAccessible(true);
      if (f.getName().equals("contract_signatureDate")||f.getName().equals("contract_workStartDate")||f.getName().equals("contract_endDate")){
        map.put(f.getName().replace("_", "."), formatDate((String) f.get(pojo)));
      }
      else {
        map.put(f.getName().replace("_", "."), (String) f.get(pojo));
      }
    }

    //removing default redundant fields
    map.remove("worker.personalCode");
    map.remove("ACTION");
    map.remove("pay.amount");
    map.remove("pay.currency");
    map.remove("pay.effectiveFrom");
    map.remove("pay.effectiveTo");
    map.remove("compensation.amount");
    map.remove("compensation.currency");
    map.remove("compensation.type");
    map.remove("compensation.effectiveFrom");
    map.remove("compensation.effectiveTo");

    return map;
  }
  private List<InputEmployeePOJO> mapFieldsFromCsv() {
    Map<String, String> mapping= new HashMap<String, String>();
    mapping.put("SystemId", "SystemId");
    mapping.put("ACTION", "ACTION");
    mapping.put("worker_name", "worker_name");
    mapping.put("worker_personalCode", "worker_personalCode");
    mapping.put("worker_gender", "worker_gender");
    mapping.put("worker_numberOfKids", "worker_numberOfKids");
    mapping.put("worker_motherMaidenName", "worker_motherMaidenName");
    mapping.put("worker_grandmotherMaidenName", "worker_grandmotherMaidenName");
    mapping.put("contract_signatureDate", "contract_signatureDate");
    mapping.put("contract_workStartDate", "contract_workStartDate");
    mapping.put("contract_type", "contract_type");
    mapping.put("contract_endDate", "contract_endDate");
    mapping.put("contract_workerId", "contract_workerId");
    mapping.put("pay_amount", "pay_amount");
    mapping.put("pay_currency", "pay_currency");
    mapping.put("pay_effectiveFrom", "pay_effectiveFrom");
    mapping.put("pay_effectiveTo", "pay_effectiveTo");
    mapping.put("compensation_amount", "compensation_amount");
    mapping.put("compensation_type", "compensation_type");
    mapping.put("compensation_currency", "compensation_currency");
    mapping.put("compensation_effectiveFrom", "compensation_effectiveFrom");
    mapping.put("compensation_effectiveTo", "compensation_effectiveTo");


    HeaderColumnNameTranslateMappingStrategy<InputEmployeePOJO> strategy =
        new HeaderColumnNameTranslateMappingStrategy<>();
    strategy.setType(InputEmployeePOJO.class);
    strategy.setColumnMapping(mapping);
    CSVReader csvReader = null;
    try {
      Resource resource = new ClassPathResource("input_01.csv");
      csvReader = new CSVReader(new FileReader
          (resource.getFile()));
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    CsvToBean csvToBean = new CsvToBean();

    return csvToBean.parse(strategy, csvReader);
  }

}
