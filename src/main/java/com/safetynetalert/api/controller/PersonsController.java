package com.safetynetalert.api.controller;

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

import com.safetynetalert.api.model.Person;
import com.safetynetalert.api.service.PersonsService;
import com.safetynetalert.api.service.dto.ChildAlertDTO;
import com.safetynetalert.api.service.dto.FireDTO;
import com.safetynetalert.api.service.dto.FirestationDTO;
import com.safetynetalert.api.service.dto.FloodDTO;
import com.safetynetalert.api.service.dto.PersonInfoDTO;

import jakarta.validation.Valid;

@RestController
public class PersonsController {

	@Autowired
	private PersonsService service;

	private HttpStatus httpStatus;

	/**
	 * Lire - Donne la liste des personnes pour une caserne de pompiers
	 *
	 * @param station_number Le numéro de la caserne de pompiers
	 * @return FirestationDTO Un objet contenant la liste des personnes personnes dépendant de la caserne de pompiers,
	 * le nombre d'adultes et le nombre d'enfants âgés de moins de 19 ans
	 * - Une personne contient: prénom, nom, adresse, numéro de téléphone
	 */
	@GetMapping("/firestation")
	public ResponseEntity<FirestationDTO> getListPersonsByStation(@RequestParam(value = "stationNumber") final String station_number) {
		FirestationDTO firestation = service.getListPersonsByStation(station_number);

		if(firestation.getPersons().size() > 0) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<FirestationDTO>(firestation, httpStatus);
	}

	/**
	 * Lire - Donne une liste d'enfants pour une adresse
	 *
	 * @param address Une adresse d'une ville
	 * @return ChildAlertDTO Un objet contenant la liste des enfants âgés de moins de 19 ans
	 * et une liste des autres membres du foyer
	 * - Un enfant contient: prénom, nom, âge
	 */
	@GetMapping("/childAlert")
	public ResponseEntity<List<ChildAlertDTO>> getListChildByAdress(@RequestParam(value = "address") final String address) {
		List<ChildAlertDTO> childAlert = service.getListChildByAdress(address);

		if(childAlert.size() > 0) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<List<ChildAlertDTO>>(childAlert, httpStatus);
	}

	/**
	 * Lire - Donne une liste des habitants vivant à une adresse
	 *
	 * @param address Une adresse d'une ville
	 * @return FireDTO Un objet contenant la liste des personnes vivant à une adresse
	 * et le numéro de la caserne de pompiers
	 * - Une personne contient: nom, numéro de téléphone, âge,
	 * 		liste de médicaments, liste d'allergies
	 */
	@GetMapping("/fire")
	public ResponseEntity<FireDTO> getListPersonsByAdressFire(@RequestParam(value = "address") final String address) {
		FireDTO fire = service.getListPersonsByAdressFire(address);

		if(!fire.getPersons().isEmpty()) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<FireDTO>(fire, httpStatus);
	}

	/**
	 * Lire - Donne une liste des habitants en fonction d'une liste d'adresses
	 *
	 * @param stationNumbers Une liste d'adresses
	 * @return FloodDTO Un objet contenant la liste des personnes desservies par les casernes de pompiers.
	 * Cette liste regroupe les personnes par adresse.
	 * et le numéro de la caserne de pompiers
	 * - Une personne contient: nom, numéro de téléphone, âge,
	 * 		liste de médicaments, liste d'allergies
	 */
	@GetMapping("/flood/stations")
	public ResponseEntity<List<FloodDTO>> getListPersonsByStationNumbersFlood(@RequestParam(value = "stations") final List<String> stationNumbers) {
		List<FloodDTO> flood = service.getListPersonsByStationsNumbersFlood(stationNumbers);

		if(flood.size() > 0) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<List<FloodDTO>>(flood, httpStatus);
	}

	/**
	 * Lire - Donne les informations d'une personne d'après le prénom et le nom
	 *
	 * @param firstName Prénom de la personne
	 * @param lastName Nom de la personne
	 * @return PersonInfoDTO Un objet contenant une liste de personnes en fonction
	 * du prénom et du nom. Les personnes avec le même nom sont ajoutés à la liste
	 * - Une personne contient: nom, adresse, âge, email
	 * 		liste de médicaments, liste d'allergies
	 */
	@GetMapping("/personInfo")
	public ResponseEntity<List<PersonInfoDTO>> getPersonInfoByFirstNameAndLastName(@RequestParam(value = "firstName") final String firstName, @RequestParam(value = "lastName") final String lastName) {
		List<PersonInfoDTO> personInfo = service.getPersonInfoByFirstNameAndLastName(firstName, lastName);

		if(personInfo.size() > 0) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<List<PersonInfoDTO>>(personInfo, httpStatus);
	}

	/**
	 * Lire - Donne les informations d'une personne d'après le prénom et le nom
	 *
	 * @param firstName Prénom de la personne
	 * @param lastName Nom de la personne
	 * @return Person Un objet Person
	 */
	@GetMapping("/person")
	public ResponseEntity<Optional<Person>> getPersonByFirstNameAndLastName(@RequestParam(value = "firstName") final String firstName, @RequestParam(value = "lastName") final String lastName) {
		System.out.println("****");
		Optional<Person> person = service.getPerson(firstName, lastName);

		if(!person.isEmpty()) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<Optional<Person>>(person, httpStatus);
	}

	/**
	 * Lire - Donne les informations de toutes les personnes
	 *
	 * @return List<Person> Une liste d'objets Person
	 */
	@GetMapping("/persons")
	public ResponseEntity<List<Person>> getAllPersons() {
		List<Person> persons = service.getAllPersons();

		if(persons.size() > 0) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<List<Person>>(persons, httpStatus);
	}

	/**
	 * Crée - Ajouter une nouvelle personne
	 *
	 * @param person Un objet Person
	 * @return Un objet Person enregistré
	 */
	@PostMapping("/person")
	public ResponseEntity<?> createPerson(@RequestBody @Valid final Person person) {
		try {
			Optional<Person> p = service.savePerson(person);

			if(!p.isEmpty()) {
				httpStatus = HttpStatus.CREATED;
			} else {
				httpStatus = HttpStatus.BAD_REQUEST;
				p = Optional.of(new Person());
			}

			return new ResponseEntity<Person>(p.get(), httpStatus);
		} catch (Exception e) {
			return new ResponseEntity<String>("Error when creating the person's information in the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Mise à jour - Mets à jour les informations d'une personne
	 *
	 * @param person Un objet Person
	 * @return Un objet Person modifié
	 */
	@PutMapping("/person")
	public ResponseEntity<?> updatePerson(@RequestBody @Valid final Person person) {
		try {
			Optional<Person> p = service.updatePerson(person);

			if(!p.isEmpty()) {
				httpStatus = HttpStatus.OK;
			} else {
				httpStatus = HttpStatus.BAD_REQUEST;
				p = Optional.of(new Person());
			}

			return new ResponseEntity<Person>(p.get(), httpStatus);
		} catch (Exception e) {
			return new ResponseEntity<String>("Error when updating the person's information in the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Supprime - Supprime les informations d'une personne
	 *
	 * @param person Un objet Person
	 */
	@DeleteMapping("/person")
	public ResponseEntity<String> deletePerson(@RequestBody @Valid final Person person) {
		try {
			boolean deleted = service.deletePerson(person);

			if(deleted) {
				return new ResponseEntity<String>("The person's information is not deleted from the database.", HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<String>("The person's information is not deleted from the database.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Error when deleting the person's information from the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
