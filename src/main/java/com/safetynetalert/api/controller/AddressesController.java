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

import com.safetynetalert.api.repository.DataStatic;
import com.safetynetalert.api.service.AddressesService;
import com.safetynetalert.api.logMessage.Const;
import com.safetynetalert.api.logMessage.LogMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name =  "API for CRUD operations on phones and emails.")
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
	@Operation(summary = "?firestation=<firestationNumber>")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Gives the list of telephone numbers.",
			content = { @Content(
				mediaType = "application/json",
				array = @ArraySchema(schema = @Schema(implementation = String.class)),
				examples = {@ExampleObject("[\n\t\"841-874-6512\",\n\t\"841-874-7462\"]")} )}),
		@ApiResponse(responseCode = "204", description = "The list is empty.",
			content = @Content) })
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
	@Operation(summary = "?city=<city>")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Gives the list of emails of a city.",
			content = { @Content(mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = String.class)),
			examples = {@ExampleObject("[\n\t\"java@email.com\",\n\t\"test@email.com\"]")} )}),
		@ApiResponse(responseCode = "204", description = "The list is empty.",
			content = @Content) })
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
