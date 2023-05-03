package com.safetynetalert.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynetalert.api.model.Firestation;
import com.safetynetalert.api.service.FirestationsService;

import jakarta.validation.Valid;

@RestController
public class FirestationsController {

	@Autowired
	private FirestationsService service;

	private HttpStatus httpStatus;

	/**
	 * Read - Get a list of fire station
	 *
	 * @param firestationNumber The number of the fire station
	 * @return A list of fire stations
	 */
	@GetMapping("/firestation/{firestationNumber}")
	public ResponseEntity<List<Firestation>> getFirestations(@PathVariable final String firestationNumber) {
		List<Firestation> firestations =  service.getFirestationsByStationNumber(firestationNumber);

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
		List<Firestation> firestations =  service.getAllFirestations();

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
	public ResponseEntity<?> createFirestation(@RequestBody @Valid final Firestation firestation) {
		try {
			Optional<Firestation> f = service.saveFirestation(firestation);

			if(!f.isEmpty()) {
				httpStatus = HttpStatus.CREATED;
			} else {
				httpStatus = HttpStatus.BAD_REQUEST;
				f = Optional.of(new Firestation());
			}

			return new ResponseEntity<Firestation>(f.get(), httpStatus);
		} catch (Exception e) {
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
	public ResponseEntity<?> updateFirestation(@RequestParam(value = "address") final String address, @RequestParam(value = "station") final String station, @RequestBody @Valid final Firestation firestation) {
		try {
			Optional<Firestation> f = service.updateFirestation(address, station,firestation);

			if(!f.isEmpty()) {
				httpStatus = HttpStatus.OK;
			} else {
				httpStatus = HttpStatus.BAD_REQUEST;
				f = Optional.of(new Firestation());
			}

			return new ResponseEntity<Firestation>(f.get(), httpStatus);
		} catch (Exception e) {
			return new ResponseEntity<String>("Error when updating the fire station in the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete - Delete an fire station
	 *
	 * @param firestation An object fire station
	 */
	@DeleteMapping("/firestation")
	public ResponseEntity<String> deleteFirestation(@RequestBody @Valid final Firestation firestation) {
		try {
			boolean deleted = service.deleteFirestation(firestation);

			if(deleted) {
				return new ResponseEntity<String>("The fire station is removed from the database.", HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<String>("The fire station is not removed from the database.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Error when deleting the fire station from the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
