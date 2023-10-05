package com.example.evamp;

import java.text.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EvampApplication {

	private static EmployeeService employeeService;
	public static void main(String[] args) throws ParseException, IllegalAccessException {
		SpringApplication.run(EvampApplication.class, args);
		employeeService = new EmployeeService();
		employeeService.process();
	}
}
