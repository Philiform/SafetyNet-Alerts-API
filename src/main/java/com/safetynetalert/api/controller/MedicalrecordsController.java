package com.safetynetalert.api.controller;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynetalert.api.exception.DateFutureException;
import com.safetynetalert.api.exception.InvalidDataException;
import com.safetynetalert.api.model.Medicalrecord;
import com.safetynetalert.api.service.MedicalrecordsService;
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
import jakarta.validation.Valid;

@Tag(name =  "API for CRUD operations on people's medical data.")
@RestController
public class MedicalrecordsController {

	private static final Logger log = LoggerFactory.getLogger(MedicalrecordsController.class);

	@Autowired
	private MedicalrecordsService service;

	private String classe = Const.CLASSE_CONTROLLER + Const.CLASSE_MEDICALRECORD;
	private String endpoint = "";
	private String method = "";
	private HttpStatus httpStatus;

	/**
	 * Read - Get the medical data of a person
	 *
	 * @param firstName The first name of the medicalrecord
	 * @param lastName The last name of the medicalrecord
	 * @return A list of the medicalrecord
	 */
	@Operation(summary = "?firstName=<firstName>&lastName=<lastName>")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Gives the person's medical data.",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = Medicalrecord.class)) }),
		@ApiResponse(responseCode = "204", description = "The list is empty.",
			content = @Content) })
	@GetMapping("/medicalRecord")
	public ResponseEntity<Optional<Medicalrecord>> getMedicalrecordsByFistNameAndLastName(@RequestParam(value = "firstName") final String firstName, @RequestParam(value = "lastName") final String lastName) {
		endpoint = "/medicalRecord";
		method = "getMedicalrecordsByFistNameAndLastName";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		Optional<Medicalrecord> medicalrecord =  service.getMedicalrecordByFirstNameAndLastName(firstName, lastName);

		log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

		if(!medicalrecord.isEmpty()) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<Optional<Medicalrecord>>(medicalrecord, httpStatus);
	}

	/**
	 * Read - Get a list of all Medicalrecord
	 *
	 * @return A list of all Medicalrecord
	 */
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Gives the medical data of all persons.",
			content = { @Content(mediaType = "application/json",
				array = @ArraySchema(schema = @Schema(implementation = Medicalrecord.class))) }),
		@ApiResponse(responseCode = "204", description = "The list is empty.",
			content = @Content) })
	@GetMapping("/medicalRecords")
	public ResponseEntity<List<Medicalrecord>> getAllMedicalrecords() {
		endpoint = "/medicalRecords";
		method = "getAllMedicalrecords";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		List<Medicalrecord> medicalrecords =  service.getAllMedicalrecords();

		log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

		if(medicalrecords.size() > 0) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<List<Medicalrecord>>(medicalrecords, httpStatus);
	}

	/**
	 * Create - Add a new medical data of a person
	 *
	 * @param medicalrecord An object medicalrecord
	 * @return The medicalrecord object saved
	 * @throws DateFutureException
	 */
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Create the person's medical data.",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = Medicalrecord.class)) }),
		@ApiResponse(responseCode = "400", description = "Bad Request",
			content = { @Content(
				schema = @Schema(implementation = String.class),
				examples = {@ExampleObject("Birthdate 14/23/2020 is not valide (valide date: mm/dd/yyyy)\nOR\n" +
					"Birthdate 12/23/3020 is not valide (must be today or in the past)\nOR\n" +
					"The medical data is not created in the database.\nOR\n" +
					"Error when creating the person's medical data in the database.\n" +
					"It already exists.")} )}) })
	@PostMapping(value = "/medicalRecord")
	public ResponseEntity<?> createMedicalrecord(@RequestBody @Valid final Medicalrecord medicalrecord, BindingResult bindingResult) throws DateFutureException {
		endpoint = "/medicalRecord";
		method = "createMedicalrecord";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		// verify birthdate
		try {
			service.isBirthdateValid(medicalrecord.getBirthdate());
		} catch (DateTimeParseException ex) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Birthdate " + medicalrecord.getBirthdate() + " is not valide (valide date: mm/dd/yyyy)", HttpStatus.BAD_REQUEST);
		} catch (DateFutureException ef) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Birthdate " + medicalrecord.getBirthdate() + " is not valide (" + ef.getMessage() + ")", HttpStatus.BAD_REQUEST);
		}

		if (bindingResult.hasErrors()) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));
			throw new InvalidDataException(bindingResult);
		}

		try {
			Optional<Medicalrecord> m = service.saveMedicalrecord(medicalrecord);

			if(!m.isEmpty()) {
				log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

				httpStatus = HttpStatus.CREATED;
			} else {
				log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

				httpStatus = HttpStatus.BAD_REQUEST;
				m = Optional.of(new Medicalrecord());
			}

			return new ResponseEntity<Medicalrecord>(m.get(), httpStatus);
		} catch (Exception e) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Error when creating the medical data in the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Update - Update an existing fire station
	 *
	 * @param medicalrecord An object medicalrecord
	 * @return The medicalrecord object modified
	 * @throws DateFutureException
	 */
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Update a person's medical data.",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = Medicalrecord.class)) }),
		@ApiResponse(responseCode = "400", description = "Bad Request",
			content = { @Content(
				schema = @Schema(implementation = String.class),
				examples = {@ExampleObject("Birthdate 14/23/2020 is not valide (valide date: mm/dd/yyyy)\nOR\n" +
					"Birthdate 12/23/3020 is not valide (must be today or in the past)\nOR\n" +
					"The medical data is not updated in the database.\nOR\n" +
					"Error when updating the person's medical data in the database.\n" +
					"It does not exist.")} )}) })
	@PutMapping("/medicalRecord")
	public ResponseEntity<?> updateMedicalrecord(@RequestBody @Valid final Medicalrecord medicalrecord, BindingResult bindingResult) throws DateFutureException {
		endpoint = "/medicalRecord";
		method = "updateMedicalrecord";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		// verify birthdate
		try {
			service.isBirthdateValid(medicalrecord.getBirthdate());
		} catch (DateTimeParseException ex) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Birthdate " + medicalrecord.getBirthdate() + " is not valide (valide date: mm/dd/yyyy)", HttpStatus.BAD_REQUEST);
		} catch (DateFutureException ef) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Birthdate " + medicalrecord.getBirthdate() + " is not valide (" + ef.getMessage() + ")", HttpStatus.BAD_REQUEST);
		}

		if (bindingResult.hasErrors()) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));
			throw new InvalidDataException(bindingResult);
		}

		try {
			Optional<Medicalrecord> m = service.updateMedicalrecord(medicalrecord);

			if(!m.isEmpty()) {
				log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

				httpStatus = HttpStatus.OK;
			} else {
				log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

				httpStatus = HttpStatus.BAD_REQUEST;
				m = Optional.of(new Medicalrecord());
			}

			return new ResponseEntity<Medicalrecord>(m.get(), httpStatus);
		} catch (Exception e) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Error when updating the medical data in the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete - Delete an fire station
	 *
	 * @param medicalrecord An object medicalrecord
	 */
	@ApiResponses(value = {
		@ApiResponse(responseCode = "202", description = "Delete a person's medical data.",
			content = { @Content(
				schema = @Schema(implementation = String.class),
				examples = {@ExampleObject("The medical data is deleted from the database.")} )}),
		@ApiResponse(responseCode = "400", description = "Bad Request",
			content = { @Content(
				schema = @Schema(implementation = String.class),
				examples = {@ExampleObject("The medical data is not deleted from the database.\nOR\n" +
					"Error when deleting the person's medical data from the database.\n" +
					"It does not exist.")} )}) })
	@DeleteMapping("/medicalRecord")
	public ResponseEntity<String> deleteMedicalrecord(@RequestBody @Valid final Medicalrecord medicalrecord, BindingResult bindingResult) {
		endpoint = "/medicalRecord";
		method = "deleteMedicalrecord";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		if (bindingResult.hasErrors()) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));
			throw new InvalidDataException(bindingResult);
		}

		try {
			boolean deleted = service.deleteMedicalrecord(medicalrecord);

			if(deleted) {
				log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

				return new ResponseEntity<String>("The medical data is deleted from the database.", HttpStatus.ACCEPTED);
			} else {
				log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

				return new ResponseEntity<String>("The medical data is not deleted from the database.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Error when deleting the person's medical data from the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
