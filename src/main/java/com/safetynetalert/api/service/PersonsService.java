package com.safetynetalert.api.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynetalert.api.exception.ObjectAlreadyExistInDBException;
import com.safetynetalert.api.exception.ObjectNotExistInDBException;
import com.safetynetalert.api.model.Medicalrecord;
import com.safetynetalert.api.model.Person;
import com.safetynetalert.api.repository.IFirestationsRepository;
import com.safetynetalert.api.repository.IMedicalrecordsRepository;
import com.safetynetalert.api.repository.IPersonsRepository;
import com.safetynetalert.api.service.dto.ChildAlertDTO;
import com.safetynetalert.api.service.dto.ChildFamilyDTO;
import com.safetynetalert.api.service.dto.FireDTO;
import com.safetynetalert.api.service.dto.FirestationDTO;
import com.safetynetalert.api.service.dto.FloodDTO;
import com.safetynetalert.api.service.dto.PersonFireFloodDTO;
import com.safetynetalert.api.service.dto.PersonFirestationDTO;
import com.safetynetalert.api.service.dto.PersonInfoDTO;
import com.safetynetalert.api.service.dto.PersonMedicalRecordDTO;

@Service
public class PersonsService {

	@Autowired
	private IPersonsRepository repositoryPersons;

	@Autowired
	private IFirestationsRepository repositoryFirestations;

	@Autowired
	private IMedicalrecordsRepository repositoryMedicalrecords;

    private PersonAge personAge = new PersonAge();

	/**
	 *
	 * @param stationNumber numéro de la caserne de pompiers
	 * @return PersonFirestationDTO DTO contenant une liste de personnes,
	 * le nombre d'adultes et le nombre d'enfants
	 *   - une personne est constituée de: prénom, nom, adresse, numéro de téléphone
	 *
	 */
	public FirestationDTO getListPersonsByStation(final String stationNumber) {
		FirestationDTO firestationDTO = new FirestationDTO();
		List<PersonFirestationDTO> personsFirestationDTO = new ArrayList<>();
		List<String> addresses = new ArrayList<>();
		List<Person> persons = new ArrayList<>();
		long numberAdult = 0;
		long numberChild = 0;

		// récupérer toutes les adresses d'une caserne
		addresses = repositoryFirestations.getAddressesByStationNumber(stationNumber);

		// récupérer toutes les personnes à ces adresses
		for (String address : addresses) {
			persons.addAll(repositoryPersons.getPersonsByAddress(address));
		}

		// récupérer nom, prénom, adresse et numéro de téléphone pour chaque personne
		for (Person person : persons) {
			PersonFirestationDTO p = new PersonFirestationDTO();
			p.setFirstName(person.getFirstName());
			p.setLastName(person.getLastName());
			p.setAddress(person.getAddress());
			p.setPhone(person.getPhone());

			// enregistrer une nouvelle personne
			personsFirestationDTO.add(p);

			// définir le nombre d'adultes
			if(personAge.isAdult(repositoryMedicalrecords.getBirthdateByFirstNameAndLastName(
					person.getFirstName(),
					person.getLastName()).get())) {
				numberAdult++;
			}
		}

		// trier la liste de personnes par adresse, nom et prénom
		Comparator<PersonFirestationDTO> personsComparator = Comparator.comparing(PersonFirestationDTO::getAddress)
                .thenComparing(PersonFirestationDTO::getLastName)
                .thenComparing(PersonFirestationDTO::getFirstName);
		personsFirestationDTO.sort(personsComparator);

		// définir le nombre d'enfantes
		numberChild = persons.size() - numberAdult;

		// enregistrer les informations dans le DTO
		firestationDTO.setPersons(personsFirestationDTO);
		firestationDTO.setNumberAdult(numberAdult);
		firestationDTO.setNumberChild(numberChild);

		// retourner le DTO rempli
		return firestationDTO;
	}

	/**
	 *
	 * @param address
	 * @return ChildAlertDTO contenant une liste d'enfants
	 *   - un enfant est constitué de: prénom, nom, age, liste des autres membres de la famille
	 *   - un membre de la famille est constitué de: prénom, nom
	 */
	public List<ChildAlertDTO> getListChildByAdress(final String address) {
		List<ChildAlertDTO> listChildAlertDTO = new ArrayList<>();
		List<ChildFamilyDTO> childFamily;
		List<Person> persons = new ArrayList<>();
		List<ChildFamilyDTO> childrenFamilyTemp = new ArrayList<>();
		Byte age;

		// récupérer toutes les personnes à cette adresse
		persons = repositoryPersons.getPersonsByAddress(address);

		// trier la liste de personnes par nom et prénom
		Comparator<Person> personsComparator = Comparator.comparing(Person::getLastName)
				.thenComparing(Person::getFirstName);
		persons.sort(personsComparator);

		// trouver l'age de toutes les personnes
		for (Person person : persons) {
			age = ageCalcul(person.getFirstName(), person.getLastName());

			// trouver un enfant
			if(age < 19) {
				ChildAlertDTO c = new ChildAlertDTO();
				c.setFirstName(person.getFirstName());
				c.setLastName(person.getLastName());
				c.setAge(age.toString());

				// enregistrer un nouvel enfant
				listChildAlertDTO.add(c);
			} else {
				ChildFamilyDTO cf = new ChildFamilyDTO();
				cf.setFirstName(person.getFirstName());
				cf.setLastName(person.getLastName());

				// enregistrer un nouvel adulte temporaire
				childrenFamilyTemp.add(cf);
			}
		}

		// trouver la famille d'un enfant
		for (ChildAlertDTO child : listChildAlertDTO) {
			childFamily = new ArrayList<>();

			// parcourir la liste des adultes
			for (ChildFamilyDTO cf : childrenFamilyTemp) {

				// récupérer les adultes ayant le même nom de famille que l'enfant
				if(cf.getLastName().equals(child.getLastName())) {
					childFamily.add(cf);
				}
			}

			// enregistrer les adultes de la famille de l'enfant
			child.setFamily(childFamily);
		}

		// retourner le DTO rempli
		return listChildAlertDTO;
	}

	/**
	 *
	 * @param address
	 * @return FireDTO contenant une liste de personnes et le numéro de la caserne de pompiers
	 *   - une personne est constitué de: nom, numéro de téléphone, age et liste des données médicales
	 *   - une donnée médicale est constitué de: médicaments et allergies
	 */
	public FireDTO getListPersonsByAdressFire(final String address) {
		FireDTO fireDTO = new FireDTO();
		List<PersonFireFloodDTO> personFireFlood = new ArrayList<>();

		// récupérer la liste des personnes selon l'adresse
		listPersonFireFlood(address, personFireFlood);

		// enregistrer les informations dans le DTO
		fireDTO.setPersons(personFireFlood);
		fireDTO.setFirestationNumber(repositoryFirestations.getStationsNumbersByAdress(address));

		// retourner le DTO rempli
		return fireDTO;
	}


	/**
	 *
	 * @param stationNumbers: liste
	 * @return FloodDTO contenant une liste d'adresses
	 *   - une adresses est constituée de: rue, une liste de personnes
	 *   - une personne est constituée de: nom, numéro de téléphone, age et liste des données médicales
	 *   - une donnée médicale est constituée de: médicaments et allergies
	 */
	public List<FloodDTO> getListPersonsByStationsNumbersFlood(final List<String> stationNumbers) {
		List<FloodDTO> listFloodDTO = new ArrayList<>();
		Set<String> listAdresses = new TreeSet<>();

		// récupérer les adresses pour les numéros de casernes de pompiers
		for(String stationNumber : stationNumbers) {
			listAdresses.addAll(repositoryFirestations.getAddressesByStationNumber(stationNumber));
		}

		// parcourir la liste des adresses
		for(String a : listAdresses) {
			FloodDTO address = new FloodDTO();
			List<PersonFireFloodDTO> personFireFlood = new ArrayList<>();

			// récupérer la liste des personnes selon la liste des adresses
			listPersonFireFlood(a, personFireFlood);

			// enregistrer les informations dans l'adresse
			address.setAddress(a);
			address.setPersons(personFireFlood);

			// enregistrer les informations dans la liste des adresses
			listFloodDTO.add(address);
		}

		// retourner le DTO rempli
		return listFloodDTO;
	}

	/**
	 *
	 * @param firstName
	 * @param lastName
	 * @return PersonInfoDTO contenant une liste de personnes
	 *   - une personne est constitué de: nom, adresse, age, email et données médicales
	 *   - une donnée médicale est constitué de: médicaments et allergies
	 */
	public List<PersonInfoDTO> getPersonInfoByFirstNameAndLastName(final String firstName, final String lastName) {
		List<PersonInfoDTO> listPersonInfoDTO = new ArrayList<>();
		List<Person> listPersons = new ArrayList<>();

		// récupérer toutes les personnes avec le même nom
		listPersons.addAll(repositoryPersons.getPersonsByLastName(lastName));
		listPersons.remove(null);

		// parcourir la liste des personnes
		for(Person person : listPersons) {
			PersonInfoDTO personInfoDTO = new PersonInfoDTO();

			// récupérer les informations de la personne
			Optional<Medicalrecord> medicalrecord = repositoryMedicalrecords.getMedicalrecordByFirstNameAndLastName(person.getFirstName(), person.getLastName());

			// récupérer données médicales
			Optional<PersonMedicalRecordDTO> personMedicalRecordDTO = Optional.of(new PersonMedicalRecordDTO());
			personMedicalRecordDTO.get().setMedications(medicalrecord.get().getMedications());
			personMedicalRecordDTO.get().setAllergies(medicalrecord.get().getAllergies());

			// enregistrer les informations dans PersonInfo
			personInfoDTO.setLastName(person.getLastName());
			personInfoDTO.setAddress(person.getAddress());
			personInfoDTO.setAge(ageCalcul(person.getFirstName(), person.getLastName()).toString());
			personInfoDTO.setEmail(person.getEmail());
			personInfoDTO.setMedicalRecord(personMedicalRecordDTO.get());

			// ajouter un nouveau personInfo avec le prénom recherché au début
			// SINON ajouter un nouveau personInfo sans le prénom recherché à la fin
			if(person.getFirstName().equals(firstName)) {
				listPersonInfoDTO.add(0, personInfoDTO);
			} else {
				listPersonInfoDTO.add(personInfoDTO);
			}
		}

		// retourner le DTO rempli
		return listPersonInfoDTO;
	}

	public Optional<Person> getPerson(final String firstName, final String lastName) {
		return repositoryPersons.getPersonByFirstNameAndLastName(firstName, lastName);
	}

	public List<Person> getAllPersons() {
		return repositoryPersons.getAllPersons();
	}

	public Optional<Person> savePerson(final Person person) throws ObjectAlreadyExistInDBException {
		if(repositoryPersons.isExist(person)) {
			throw new ObjectAlreadyExistInDBException("It already exists.");
		}
		return repositoryPersons.savePerson(person);
	}

	public Optional<Person> updatePerson(final Person person) throws ObjectNotExistInDBException {
		if(getPerson(person.getFirstName(), person.getLastName()).isEmpty()) {
			throw new ObjectNotExistInDBException("It does not exist.");
		}
		return repositoryPersons.updatePerson(person);
	}

	public boolean deletePerson(final Person person) throws ObjectNotExistInDBException {
		if(!repositoryPersons.isExist(person)) {
			throw new ObjectNotExistInDBException("It does not exist.");
		}
		return repositoryPersons.deletePerson(person);
	}

	/**
	 *
	 * @param firstName
	 * @param lastName
	 * @return Byte: age de la personne
	 */
	private Byte ageCalcul(final String firstName, final String lastName) {
		return personAge.getAge(repositoryMedicalrecords.getBirthdateByFirstNameAndLastName(firstName, lastName).get());
	}

	/**
	 *
	 * @param address
	 * @param personFireFlood contient une liste de PersonFireFlood
	 * PersonFireFlood contient nom, numéro de téléphone, age et medicalRecord
	 * medicalRecord contient une liste de médicaments et une liste d'allergies
	 */
	private void listPersonFireFlood(final String address, List<PersonFireFloodDTO> personFireFlood) {
		List<Person> persons = new ArrayList<>();

		// récupérer toutes les personnes à cette adresse
		persons = repositoryPersons.getPersonsByAddress(address);

		// trier la liste de personnes par nom
		Comparator<Person> personsComparator = Comparator.comparing(Person::getLastName);
		persons.sort(personsComparator);

		// récupérer nom, numéro de téléphone, et âge pour chaque personne
		for (Person person : persons) {
			PersonFireFloodDTO p = new PersonFireFloodDTO();
			String lastName = person.getLastName();
			String firstName = person.getFirstName();
			Byte age;

			p.setLastName(lastName);
			p.setPhone(person.getPhone());
			age = ageCalcul(firstName, lastName);
			p.setAge(age.toString());

			// récupérer données médicales pour chaque personne
			p.setMedicalRecord(getMedicalRecord(firstName, lastName));

			// enregistrer une nouvelle personne
			personFireFlood.add(p);
		}
	}

	/**
	 *
	 * @param firstName
	 * @param lastName
	 * @return PersonMedicalRecord qui contient une liste de médicaments
	 * et une liste d'allergies
	 */
	private PersonMedicalRecordDTO getMedicalRecord(final String firstName, final String lastName) {
		// récupérer données médicales d'une personne
		PersonMedicalRecordDTO personMedicalRecordDTO = new PersonMedicalRecordDTO();
		Medicalrecord medicalrecord = new Medicalrecord();

		medicalrecord = repositoryMedicalrecords.getMedicalrecordByFirstNameAndLastName(firstName, lastName).get();
		personMedicalRecordDTO.setMedications(medicalrecord.getMedications());
		personMedicalRecordDTO.setAllergies(medicalrecord.getAllergies());

		return personMedicalRecordDTO;
	}
}
