package com.safetynetalert.api.controller;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynetalert.api.exception.DateFutureException;
import com.safetynetalert.api.model.Medicalrecord;
import com.safetynetalert.api.service.MedicalrecordsService;

import jakarta.validation.Valid;

@RestController
public class MedicalrecordsController {

	@Autowired
	private MedicalrecordsService service;

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
		Optional<Medicalrecord> medicalrecord =  service.getMedicalrecordByFirstNameAndLastName(firstName, lastName);

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
		List<Medicalrecord> medicalrecords =  service.getAllMedicalrecords();

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
	public ResponseEntity<?> createMedicalrecord(@RequestBody @Valid final Medicalrecord medicalrecord) throws DateFutureException {
		// verify birthdate
		try {
			service.isBirthdateValid(medicalrecord.getBirthdate());
		} catch (DateTimeParseException ex) {
			return new ResponseEntity<String>("Birthdate " + medicalrecord.getBirthdate() + " is not valide (valide date: mm/dd/yyyy)", HttpStatus.BAD_REQUEST);
		} catch (DateFutureException ef) {
			return new ResponseEntity<String>("Birthdate " + medicalrecord.getBirthdate() + " is not valide (" + ef.getMessage() + ")", HttpStatus.BAD_REQUEST);
		}

		try {
			Optional<Medicalrecord> m = service.saveMedicalrecord(medicalrecord);

			if(!m.isEmpty()) {
				httpStatus = HttpStatus.CREATED;
			} else {
				httpStatus = HttpStatus.BAD_REQUEST;
				m = Optional.of(new Medicalrecord());
			}

			return new ResponseEntity<Medicalrecord>(m.get(), httpStatus);
		} catch (Exception e) {
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
	public ResponseEntity<?> updateMedicalrecord(@RequestBody @Valid final Medicalrecord medicalrecord) throws DateFutureException {
		// verify birthdate
		try {
			service.isBirthdateValid(medicalrecord.getBirthdate());
		} catch (DateTimeParseException ex) {
			return new ResponseEntity<String>("Birthdate " + medicalrecord.getBirthdate() + " is not valide (valide date: mm/dd/yyyy)", HttpStatus.BAD_REQUEST);
		} catch (DateFutureException ef) {
			return new ResponseEntity<String>("Birthdate " + medicalrecord.getBirthdate() + " is not valide (" + ef.getMessage() + ")", HttpStatus.BAD_REQUEST);
		}

		try {
			Optional<Medicalrecord> m = service.updateMedicalrecord(medicalrecord);

			if(!m.isEmpty()) {
				httpStatus = HttpStatus.OK;
			} else {
				httpStatus = HttpStatus.BAD_REQUEST;
				m = Optional.of(new Medicalrecord());
			}

			return new ResponseEntity<Medicalrecord>(m.get(), httpStatus);
		} catch (Exception e) {
			return new ResponseEntity<String>("Error when updating the medical data in the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete - Delete an fire station
	 *
	 * @param medicalrecord An object medicalrecord
	 */
	@DeleteMapping("/medicalRecord")
	public ResponseEntity<String> deleteMedicalrecord(@RequestBody @Valid final Medicalrecord medicalrecord) {
		try {
			boolean deleted = service.deleteMedicalrecord(medicalrecord);

			if(deleted) {
				return new ResponseEntity<String>("The medical data is deleted from the database.", HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<String>("The medical data is deleted from the database.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Error when deleting the person's medical data from the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
