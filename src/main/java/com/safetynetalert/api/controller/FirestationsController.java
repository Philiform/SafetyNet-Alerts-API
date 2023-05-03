package com.safetynetalert.api.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynetalert.api.exception.InvalidDataException;
import com.safetynetalert.api.logMessage.Const;
import com.safetynetalert.api.logMessage.LogMessage;
import com.safetynetalert.api.model.Firestation;
import com.safetynetalert.api.repository.DataStatic;
import com.safetynetalert.api.service.FirestationsService;

import jakarta.validation.Valid;

@RestController
public class FirestationsController {

	private static final Logger log = LoggerFactory.getLogger(DataStatic.class);

	@Autowired
	private FirestationsService service;

	private String classe = Const.CLASSE_CONTROLLER + Const.CLASSE_FIRESTATION;
	private String endpoint = "";
	private String method = "";
	private HttpStatus httpStatus;

	/**
	 * Read - Get a list of fire station
	 *
	 * @param firestationNumber The number of the fire station
	 * @return A list of fire stations
	 */
	@GetMapping("/firestation/{firestationNumber}")
	public ResponseEntity<List<Firestation>> getFirestations(@PathVariable final String firestationNumber) {
		endpoint = "/firestation/{firestationNumber}";
		method = "getFirestations";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		List<Firestation> firestations =  service.getFirestationsByStationNumber(firestationNumber);

		log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

		if(firestations.size() > 0) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<List<Firestation>>(firestations, httpStatus);
	}

	/**
	 * Read - Get a list of all fire station
	 *
	 * @return A list of all fire stations
	 */
	@GetMapping("/firestations")
	public ResponseEntity<List<Firestation>> getAllFirestations() {
		endpoint = "/firestations";
		method = "getAllFirestations";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		List<Firestation> firestations =  service.getAllFirestations();

		log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

		if(firestations.size() > 0) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<List<Firestation>>(firestations, httpStatus);
	}

	/**
	 * Create - Add a new fire station
	 *
	 * @param firestation An object fire station
	 * @return The fire station object saved
	 */
	@PostMapping("/firestation")
	public ResponseEntity<?> createFirestation(@RequestBody @Valid final Firestation firestation, BindingResult bindingResult) {
		endpoint = "/firestation";
		method = "createFirestation";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		if (bindingResult.hasErrors()) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));
			throw new InvalidDataException(bindingResult);
		}

		try {
			Optional<Firestation> f = service.saveFirestation(firestation);

			if(!f.isEmpty()) {
				log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

				httpStatus = HttpStatus.CREATED;
			} else {
				log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

				httpStatus = HttpStatus.BAD_REQUEST;
				f = Optional.of(new Firestation());
			}

			return new ResponseEntity<Firestation>(f.get(), httpStatus);
		} catch (Exception e) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Error when saving the firestation in the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Update - Update an existing fire station
	 *
	 * @param address       	- The address of the fire station to update
	 * @param firestationNumber - The firestationNumber of the fire station to update
	 * @param firestation		- The fire station object updated
	 * @return
	 */
	@PutMapping("/firestation")
	public ResponseEntity<?> updateFirestation(@RequestParam(value = "address") final String address, @RequestParam(value = "station") final String station, @RequestBody @Valid final Firestation firestation, BindingResult bindingResult) {
		endpoint = "/firestation";
		method = "updateFirestation";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		if (bindingResult.hasErrors()) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));
			throw new InvalidDataException(bindingResult);
		}

		try {
			Optional<Firestation> f = service.updateFirestation(address, station,firestation);

			if(!f.isEmpty()) {
				log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

				httpStatus = HttpStatus.OK;
			} else {
				log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

				httpStatus = HttpStatus.BAD_REQUEST;
				f = Optional.of(new Firestation());
			}

			return new ResponseEntity<Firestation>(f.get(), httpStatus);
		} catch (Exception e) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Error when updating the fire station in the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete - Delete an fire station
	 *
	 * @param firestation An object fire station
	 */
	@DeleteMapping("/firestation")
	public ResponseEntity<String> deleteFirestation(@RequestBody @Valid final Firestation firestation, BindingResult bindingResult) {
		endpoint = "/firestation";
		method = "deleteFirestation";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		if (bindingResult.hasErrors()) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));
			throw new InvalidDataException(bindingResult);
		}

		try {
			boolean deleted = service.deleteFirestation(firestation);

			if(deleted) {
				log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

				return new ResponseEntity<String>("The fire station is removed from the database.", HttpStatus.ACCEPTED);
			} else {
				log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

				return new ResponseEntity<String>("The fire station is not removed from the database.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Error when deleting the fire station from the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
