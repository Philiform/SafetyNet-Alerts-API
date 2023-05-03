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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynetalert.api.exception.InvalidDataException;
import com.safetynetalert.api.model.Person;
import com.safetynetalert.api.service.PersonsService;
import com.safetynetalert.api.service.dto.ChildAlertDTO;
import com.safetynetalert.api.service.dto.FireDTO;
import com.safetynetalert.api.service.dto.FirestationDTO;
import com.safetynetalert.api.service.dto.FloodDTO;
import com.safetynetalert.api.service.dto.PersonInfoDTO;
import com.safetynetalert.api.logMessage.Const;
import com.safetynetalert.api.logMessage.LogMessage;

import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@OpenAPIDefinition(
        info = @Info(
                title = "SafetyNetAlerts API",
                version = "0.0.1",
                description = "This API Rest allows:\r\n"
                		+ "- in case of a fire to provide information about the people in the burning building, such as their phone numbers. \r\n"
                		+ "- in case of a hurricane alert to provide the emails of the inhabitants of the area.\r\n"
                		+ "- in case of a flood to provide specific information (ages and their medical history: treatments, allergies, etc.) about people in the area."))

@Tag(name =  "API for CRUD operations on people.")
@RestController
public class PersonsController {

	private static final Logger log = LoggerFactory.getLogger(PersonsController.class);

	@Autowired
	private PersonsService service;

	private String classe = Const.CLASSE_CONTROLLER + Const.CLASSE_PERSON;
	private String endpoint = "";
	private String method = "";
	private HttpStatus httpStatus;

	/**
	 * Lire - Donne la liste des personnes pour une caserne de pompiers
	 *
	 * @param station_number Le numéro de la caserne de pompiers
	 * @return FirestationDTO Un objet contenant la liste des personnes personnes dépendant de la caserne de pompiers,
	 * le nombre d'adultes et le nombre d'enfants âgés de moins de 19 ans
	 * - Une personne contient: prénom, nom, adresse, numéro de téléphone
	 */
	@Operation(summary = "?stationNumber=<stationNumber>")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Gives the list of" +
				" persons covered by the corresponding fire station." +
				" The list includes the following specific information: first name," +
				" name, address, phone number. In addition, it provides" +
				" a count of the number of adults and the number of children" +
				" (any individual 18 years of age or younger) in the service area.",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = FirestationDTO.class)) }),
		@ApiResponse(responseCode = "204", description = "The list is empty.",
			content = @Content) })
	@GetMapping("/firestation")
	public ResponseEntity<FirestationDTO> getListPersonsByStation(@RequestParam(value = "stationNumber") final String station_number) {
		endpoint = "/firestation";
		method = "getListPersonsByStation";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		FirestationDTO firestation = service.getListPersonsByStation(station_number);

		log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

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
	@Operation(summary = "?address=<address>")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Gives the list of" +
				" children (any individual 18 years of age or younger) living at this" +
				" address. The list includes each child's first and last name," +
				" age and a list of other household members.",
			content = { @Content(mediaType = "application/json",
				array = @ArraySchema(schema = @Schema(implementation = ChildAlertDTO.class))) }),
		@ApiResponse(responseCode = "204", description = "The list is empty.",
			content = @Content) })
	@GetMapping("/childAlert")
	public ResponseEntity<List<ChildAlertDTO>> getListChildByAdress(@RequestParam(value = "address") final String address) {
		endpoint = "/childAlert";
		method = "getListChildByAdress";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		List<ChildAlertDTO> childAlert = service.getListChildByAdress(address);

		log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

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
	@Operation(summary = "?address=<address>")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Gives the list of" +
				" residents living at the given address and the number of the" +
				" the fire station serving it. The list includes name," +
				" phone number, age and medical history (medications, dosage and" +
				" (medications, dosage and allergies) of each person.",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = FireDTO.class)) }),
		@ApiResponse(responseCode = "204", description = "The list is empty.",
			content = @Content) })
	@GetMapping("/fire")
	public ResponseEntity<FireDTO> getListPersonsByAdressFire(@RequestParam(value = "address") final String address) {
		endpoint = "/fire";
		method = "getListPersonsByAdressFire";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		FireDTO fire = service.getListPersonsByAdressFire(address);

		log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

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
	@Operation(summary = "?stations=<a list of station_numbers>")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Gives the list of" +
				" all households served by the fire station. This list" +
				" groups people by address. It includes" +
				" name, phone number and age of residents, and" +
				" medical history (medications, and allergies) next to each name.",
			content = { @Content(mediaType = "application/json",
				array = @ArraySchema(schema = @Schema(implementation = FloodDTO.class))) }),
		@ApiResponse(responseCode = "204", description = "The list is empty.",
			content = @Content) })
	@GetMapping("/flood/stations")
	public ResponseEntity<List<FloodDTO>> getListPersonsByStationNumbersFlood(@RequestParam(value = "stations") final List<String> stationNumbers) {
		endpoint = "/flood/stations";
		method = "getListPersonsByStationNumbersFlood";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		List<FloodDTO> flood = service.getListPersonsByStationsNumbersFlood(stationNumbers);

		log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

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
	@Operation(summary = "?firstName=<firstName>&lastName=<lastName>")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Give name," +
				" address, age, email address and medical history" +
				" (medications, dosage, allergies) of each resident." +
				" If more than one person has the same name, they will all appear.",
			content = { @Content(mediaType = "application/json",
				array = @ArraySchema(schema = @Schema(implementation = PersonInfoDTO.class))) }),
		@ApiResponse(responseCode = "204", description = "The list is empty.",
			content = @Content) })
	@GetMapping("/personInfo")
	public ResponseEntity<List<PersonInfoDTO>> getPersonInfoByFirstNameAndLastName(@RequestParam(value = "firstName") final String firstName, @RequestParam(value = "lastName") final String lastName) {
		endpoint = "/personInfo";
		method = "getPersonInfoByFirstNameAndLastName";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		List<PersonInfoDTO> personInfo = service.getPersonInfoByFirstNameAndLastName(firstName, lastName);

		log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

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
	@Operation(summary = "?firstName=<firstName>&lastName=<lastName>")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Gives the person's information",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = Person.class)) }),
		@ApiResponse(responseCode = "204", description = "The list is empty.",
			content = @Content) })
	@GetMapping("/person")
	public ResponseEntity<Optional<Person>> getPersonByFirstNameAndLastName(@RequestParam(value = "firstName") final String firstName, @RequestParam(value = "lastName") final String lastName) {
		endpoint = "/person";
		method = "getPersonByFirstNameAndLastName";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		Optional<Person> person = service.getPerson(firstName, lastName);

		log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

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
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Gives the information of all the people",
			content = { @Content(mediaType = "application/json",
				array = @ArraySchema(schema = @Schema(implementation = Person.class))) }),
		@ApiResponse(responseCode = "204", description = "The list is empty.",
			content = @Content) })
	@GetMapping("/persons")
	public ResponseEntity<List<Person>> getAllPersons() {
		endpoint = "/persons";
		method = "getAllPersons";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		List<Person> persons = service.getAllPersons();

		log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

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
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Create the person's information.",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = Person.class)) }),
		@ApiResponse(responseCode = "400", description = "Bad Request",
			content = { @Content(
				schema = @Schema(implementation = String.class),
				examples = {@ExampleObject("The person's information is not created in the database.\nOR\n" +
					"Error when creating the person's information in the database.\n" +
					"It already exists.")} )}) })
	@PostMapping("/person")
	public ResponseEntity<?> createPerson(@RequestBody @Valid final Person person, BindingResult bindingResult) {
		endpoint = "/person";
		method = "createPerson";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		if (bindingResult.hasErrors()) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));
			throw new InvalidDataException(bindingResult);
		}

		try {
			Optional<Person> p = service.savePerson(person);

			if(!p.isEmpty()) {
				log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

				httpStatus = HttpStatus.CREATED;
			} else {
				log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

				httpStatus = HttpStatus.BAD_REQUEST;
				p = Optional.of(new Person());
			}

			return new ResponseEntity<Person>(p.get(), httpStatus);
		} catch (Exception e) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Error when creating the person's information in the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Mise à jour - Mets à jour les informations d'une personne
	 *
	 * @param person Un objet Person
	 * @return Un objet Person modifié
	 */
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Update the person's information",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = Person.class)) }),
		@ApiResponse(responseCode = "400", description = "Bad Request",
			content = { @Content(
				schema = @Schema(implementation = String.class),
				examples = {@ExampleObject("The person's information is not updated in the database.\nOR\n" +
					"Error when updating the person's information in the database.\n" +
					"It does not exist.")} )}) })
	@PutMapping("/person")
	public ResponseEntity<?> updatePerson(@RequestBody @Valid final Person person, BindingResult bindingResult) {
		endpoint = "/person";
		method = "updatePerson";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		if (bindingResult.hasErrors()) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));
			throw new InvalidDataException(bindingResult);
		}

		try {
			Optional<Person> p = service.updatePerson(person);

			if(!p.isEmpty()) {
				log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

				httpStatus = HttpStatus.OK;
			} else {
				log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

				httpStatus = HttpStatus.BAD_REQUEST;
				p = Optional.of(new Person());
			}

			return new ResponseEntity<Person>(p.get(), httpStatus);
		} catch (Exception e) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Error when updating the person's information in the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Supprime - Supprime les informations d'une personne
	 *
	 * @param person Un objet Person
	 */
	@ApiResponses(value = {
		@ApiResponse(responseCode = "202", description = "Delete the person's information",
			content = { @Content(
				schema = @Schema(implementation = String.class),
				examples = {@ExampleObject("The person's information is deleted from the database.")} )}),
		@ApiResponse(responseCode = "400", description = "Bad Request",
			content = { @Content(
				schema = @Schema(implementation = String.class),
				examples = {@ExampleObject("The person's information is not deleted from the database.\nOR\n" +
					"Error when deleting the person's information from the database.\n" +
					"It does not exist.")} )}) })
	@DeleteMapping("/person")
	public ResponseEntity<String> deletePerson(@RequestBody @Valid final Person person, BindingResult bindingResult) {
		endpoint = "/person";
		method = "deletePerson";
		log.info(LogMessage.getMessage(classe, Const.REQUETE, endpoint, method));

		if (bindingResult.hasErrors()) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));
			throw new InvalidDataException(bindingResult);
		}

		try {
			boolean deleted = service.deletePerson(person);

			if(deleted) {
				log.info(LogMessage.getMessage(classe, Const.RESPONSE_OK, endpoint, method));

				return new ResponseEntity<String>("The person's information is deleted from the database.", HttpStatus.ACCEPTED);
			} else {
				log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

				return new ResponseEntity<String>("The person's information is not deleted from the database.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error(LogMessage.getMessage(classe, Const.RESPONSE_ERREUR, endpoint, method));

			return new ResponseEntity<String>("Error when deleting the person's information from the database.\n" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
