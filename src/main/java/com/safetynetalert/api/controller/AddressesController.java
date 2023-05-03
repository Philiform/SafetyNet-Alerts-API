package com.safetynetalert.api.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynetalert.api.service.AddressesService;

@RestController
public class AddressesController {

	@Autowired
	private AddressesService service;

	private HttpStatus httpStatus;

	/**
	 *
	 * @param firestation_number
	 * @return a list of phone numbers sorted alphabetically
	 */
    @GetMapping("/phoneAlert")
	public ResponseEntity<Set<String>> getPhonesByFirestationNumber(@RequestParam(value = "firestation") final String firestationNumber) {
		Set<String> phones =  service.getPhonesByFirestationNumber(firestationNumber);

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
		Set<String> emails =  service.getEmailsByCity(city);

		if(emails.size() > 0) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<Set<String>>(emails, httpStatus);
	}
}
