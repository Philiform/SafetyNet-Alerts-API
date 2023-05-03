package com.safetynetalert.api.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynetalert.api.logMessage.Const;
import com.safetynetalert.api.logMessage.LogMessage;
import com.safetynetalert.api.repository.DataStatic;
import com.safetynetalert.api.service.AddressesService;

@RestController
public class AddressesController {

	private static final Logger log = LoggerFactory.getLogger(DataStatic.class);

	@Autowired
	private AddressesService service;

	private String classe = Const.CLASSE_CONTROLLER + Const.CLASSE_ADDRESSES;
	private String endpoint = "";
	private String method = "";
	private HttpStatus httpStatus;

	/**
	 *
	 * @param firestation_number
	 * @return a list of phone numbers sorted alphabetically
	 */
    @GetMapping("/phoneAlert")
	public ResponseEntity<Set<String>> getPhonesByFirestationNumber(@RequestParam(value = "firestation") final String firestationNumber) {
		endpoint = "/phoneAlert";
		method = "getPhonesByFirestationNumber";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		Set<String> phones =  service.getPhonesByFirestationNumber(firestationNumber);

		log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

		if(phones.size() > 0) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<Set<String>>(phones, httpStatus);
	}

	/**
	 *
	 * @param city
	 * @return a list of emails sorted in alphabetical order
	 */
	@GetMapping("/communityEmail")
	public ResponseEntity<Set<String>> getEmailsByCity(@RequestParam(value = "city") final String city) {
		endpoint = "/communityEmail";
		method = "getEmailsByCity";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		Set<String> emails =  service.getEmailsByCity(city);

		log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

		if(emails.size() > 0) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<Set<String>>(emails, httpStatus);
	}
}
