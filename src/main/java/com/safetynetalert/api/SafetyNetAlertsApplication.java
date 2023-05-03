package com.safetynetalert.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.safetynetalert.api.repository.DataStatic;

@SpringBootApplication
public class SafetyNetAlertsApplication {

    public static void main(String[] args) {

		DataStatic.loadData("data.json");

		SpringApplication.run(SafetyNetAlertsApplication.class, args);

	}

}
