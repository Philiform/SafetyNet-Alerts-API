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
import com.safetynetalert.api.logMessage.Const;
import com.safetynetalert.api.logMessage.LogMessage;
import com.safetynetalert.api.model.Medicalrecord;
import com.safetynetalert.api.service.MedicalrecordsService;

import jakarta.validation.Valid;

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

				return new ResponseEntity<String>("The medical data is deleted from the database.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Error when deleting the person's medical data from the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
