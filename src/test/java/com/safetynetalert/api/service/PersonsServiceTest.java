package com.safetynetalert.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynetalert.api.exception.ObjectAlreadyExistInDBException;
import com.safetynetalert.api.exception.ObjectNotExistInDBException;
import com.safetynetalert.api.model.Medicalrecord;
import com.safetynetalert.api.model.Person;
import com.safetynetalert.api.repository.impl.FirestationsRepositoryFileImpl;
import com.safetynetalert.api.repository.impl.MedicalrecordsRepositoryFileImpl;
import com.safetynetalert.api.repository.impl.PersonsRepositoryFileImpl;
import com.safetynetalert.api.service.dto.ChildAlertDTO;
import com.safetynetalert.api.service.dto.ChildFamilyDTO;
import com.safetynetalert.api.service.dto.FireDTO;
import com.safetynetalert.api.service.dto.FirestationDTO;
import com.safetynetalert.api.service.dto.FloodDTO;
import com.safetynetalert.api.service.dto.PersonFireFloodDTO;
import com.safetynetalert.api.service.dto.PersonFirestationDTO;
import com.safetynetalert.api.service.dto.PersonInfoDTO;
import com.safetynetalert.api.service.dto.PersonMedicalRecordDTO;

@ExtendWith(MockitoExtension.class)
class PersonsServiceTest {

	@InjectMocks
	private PersonsService service;

	@Mock
	private PersonsRepositoryFileImpl mockRepositoryPersons;

	@Mock
	private FirestationsRepositoryFileImpl mockRepositoryFirestations;

	@Mock
	private MedicalrecordsRepositoryFileImpl mockRepositoryMedicalrecords;

	private List<String> listString = new ArrayList<>();
	private static List<String> listAddresses = new ArrayList<>();

	private static Person person;
	private static Person newPerson;
	private static Person modifiedPerson;
	private static List<Person> listPersons = new ArrayList<>();

	private static List<PersonFireFloodDTO> listPersonsFireFloodDTO = new ArrayList<>();
	private static PersonFireFloodDTO personFireFloodDTO = new PersonFireFloodDTO();
	private static PersonMedicalRecordDTO personMedicalRecordDTO = new PersonMedicalRecordDTO();
	private static Medicalrecord medicalrecord = new Medicalrecord();

	private PersonAge personAge = new PersonAge();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		person = new Person();
		person.setFirstName("Peter");
		person.setLastName("Duncan");
		person.setAddress("644 Gershwin Cir");
		person.setCity("Culver");
		person.setZip("97451");
		person.setPhone("841-874-6512");
		person.setEmail("jaboyd@email.com");

		newPerson = new Person();
		newPerson.setFirstName("Phil");
		newPerson.setLastName("Finbert");
		newPerson.setAddress("4 rue du Java");
		newPerson.setCity("Poo");
		newPerson.setZip("42");
		newPerson.setPhone("06 07 08 09 01");
		newPerson.setEmail("phil@java.com");

		modifiedPerson = new Person();
		modifiedPerson.setFirstName("Phil");
		modifiedPerson.setLastName("Finbert");
		modifiedPerson.setAddress("4 rue du JavaHome");
		modifiedPerson.setCity("Poo");
		modifiedPerson.setZip("4245");
		modifiedPerson.setPhone("06 07 08 09 99");
		modifiedPerson.setEmail("phil54@java.com");

		listAddresses.add("644 Gershwin Cir");

		listPersons.add(person);

		medicalrecord.setFirstName("");
		medicalrecord.setLastName("");
		medicalrecord.setBirthdate("");
		medicalrecord.setMedications(new ArrayList<String>());
		medicalrecord.setAllergies(new ArrayList<String>());

		personMedicalRecordDTO.setMedications(new ArrayList<String>());
		personMedicalRecordDTO.setAllergies(new ArrayList<String>());

		personFireFloodDTO.setLastName(person.getLastName());
		personFireFloodDTO.setPhone(person.getPhone());
		personFireFloodDTO.setAge("0");
		personFireFloodDTO.setMedicalRecord(personMedicalRecordDTO);
		listPersonsFireFloodDTO.add(personFireFloodDTO);

	}

	@Test
	void testGivenStationIs1_WhenGetListPersonsByStation_ThenReturnFirestationDTO() {
		// GIVEN
		FirestationDTO firestationDTO = new FirestationDTO();
		FirestationDTO firestationDTOAttendu = new FirestationDTO();
		List<String> aListAddresses = new ArrayList<>();
		List<PersonFirestationDTO> persons = new ArrayList<>();
		List<Person>pListPersons1 = new ArrayList<>();
		List<Person>pListPersons2 = new ArrayList<>();
		List<Person>pListPersons3 = new ArrayList<>();

		aListAddresses.add("29 15th St");
		aListAddresses.add("892 Downing Ct");
		aListAddresses.add("951 LoneTree Rd");

		Person pJonanathan = new Person("Jonanathan", "Marrack", "29 15th St", "Culver", "97451", "841-874-6513", "drk@email.com");
		Person pSophia = new Person("Sophia", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com");
		Person pWarren = new Person("Warren", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7512", "ward@email.com");
		Person pZach = new Person("Zach", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7512", "zarc@email.com");
		Person pEric = new Person("Eric", "Cadigan", "951 LoneTree Rd", "Culver", "97451", "841-874-7458", "gramps@email.com");

		pListPersons1.add(pJonanathan);
		pListPersons2.add(pSophia);
		pListPersons2.add(pWarren);
		pListPersons2.add(pZach);
		pListPersons3.add(pEric);

		PersonFirestationDTO jonanathan = new PersonFirestationDTO("Jonanathan", "Marrack", "29 15th St", "841-874-6513");
		PersonFirestationDTO sophia = new PersonFirestationDTO("Sophia", "Zemicks", "892 Downing Ct", "841-874-7878");
		PersonFirestationDTO warren = new PersonFirestationDTO("Warren", "Zemicks", "892 Downing Ct", "841-874-7512");
		PersonFirestationDTO zach = new PersonFirestationDTO("Zach", "Zemicks", "892 Downing Ct", "841-874-7512");
		PersonFirestationDTO eric = new PersonFirestationDTO("Eric", "Cadigan", "951 LoneTree Rd", "841-874-7458");

		persons.add(jonanathan);
		persons.add(sophia);
		persons.add(warren);
		persons.add(zach);
		persons.add(eric);

		firestationDTOAttendu.setPersons(persons);
		firestationDTOAttendu.setNumberAdult(4);
		firestationDTOAttendu.setNumberChild(1);

		given(mockRepositoryFirestations.getAddressesByStationNumber(anyString())).willReturn(aListAddresses);
		given(mockRepositoryPersons.getPersonsByAddress(anyString()))
			.willReturn(pListPersons1)
			.willReturn(pListPersons2)
			.willReturn(pListPersons3);
		given(mockRepositoryMedicalrecords.getBirthdateByFirstNameAndLastName(anyString(), anyString()))
			.willReturn(Optional.of("01/03/1989"))
			.willReturn(Optional.of("03/06/1988"))
			.willReturn(Optional.of("03/06/1985"))
			.willReturn(Optional.of("03/06/2017"))
			.willReturn(Optional.of("08/06/1945"));

		// WHEN
		firestationDTO = service.getListPersonsByStation("1");

		// THEN
		verify(mockRepositoryFirestations, times(1)).getAddressesByStationNumber(anyString());
		verify(mockRepositoryPersons, times(3)).getPersonsByAddress(anyString());
		verify(mockRepositoryMedicalrecords, times(5)).getBirthdateByFirstNameAndLastName(anyString(), anyString());
		assertThat(firestationDTO).isEqualTo(firestationDTOAttendu);
	}

	@Test
	void testGivenAddressIs892DowningCt_WhenGetListChildByAdress_ThenReturnChildAlertDTO() {
		// GIVEN
		List<ChildAlertDTO> listChildAlertDTO = new ArrayList<>();
		List<ChildAlertDTO> listChildAlertDTOAttendu = new ArrayList<>();
		List<ChildFamilyDTO>childFamily = new ArrayList<>();
		List<Person>pListPersons = new ArrayList<>();

		Person pJohn = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
		Person pJacob = new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com");
		Person pTenley = new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
		Person pRoger = new Person("Roger", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
		Person pFelicia= new Person("Felicia", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6544", "jaboyd@email.com");

		pListPersons.add(pFelicia);
		pListPersons.add(pJacob);
		pListPersons.add(pJohn);
		pListPersons.add(pRoger);
		pListPersons.add(pTenley);

		ChildFamilyDTO felicia = new ChildFamilyDTO("Felicia", "Boyd");
		ChildFamilyDTO jacob = new ChildFamilyDTO("Jacob", "Boyd");
		ChildFamilyDTO john = new ChildFamilyDTO("John", "Boyd");

		childFamily.add(felicia);
		childFamily.add(jacob);
		childFamily.add(john);

		ChildAlertDTO roger = new ChildAlertDTO("Roger", "Boyd", String.valueOf(personAge.getAge("09/06/2017")), childFamily);
		ChildAlertDTO tenley = new ChildAlertDTO("Tenley", "Boyd", String.valueOf(personAge.getAge("02/18/2012")), childFamily);

		listChildAlertDTOAttendu.add(roger);
		listChildAlertDTOAttendu.add(tenley);

		given(mockRepositoryPersons.getPersonsByAddress(anyString())).willReturn(pListPersons);
		given(mockRepositoryMedicalrecords.getBirthdateByFirstNameAndLastName(anyString(), anyString()))
			.willReturn(Optional.of("01/08/1986"))
			.willReturn(Optional.of("03/06/1989"))
			.willReturn(Optional.of("03/06/1984"))
			.willReturn(Optional.of("09/06/2017"))
			.willReturn(Optional.of("02/18/2012"));

		// WHEN
		listChildAlertDTO = service.getListChildByAdress("1509 Culver St");

		// THEN
		verify(mockRepositoryPersons, times(1)).getPersonsByAddress(anyString());
		verify(mockRepositoryMedicalrecords, times(5)).getBirthdateByFirstNameAndLastName(anyString(), anyString());
		assertThat(listChildAlertDTO).isEqualTo(listChildAlertDTOAttendu);
	}

	@Test
	void testGivenAddressIs644GershwinCir_WhenGetListPersonsByAdressFire_ThenReturnFireDTO() {
		// GIVEN
		FireDTO fireDTO = new FireDTO();
		FireDTO fireDTOAttendu = new FireDTO();

		fireDTOAttendu.setPersons(listPersonsFireFloodDTO);
		fireDTOAttendu.setFirestationNumber(listString);

		given(mockRepositoryPersons.getPersonsByAddress(anyString())).willReturn(listPersons);
		given(mockRepositoryMedicalrecords.getBirthdateByFirstNameAndLastName(anyString(), anyString())).willReturn(Optional.of("02/04/2023"));
		given(mockRepositoryMedicalrecords.getMedicalrecordByFirstNameAndLastName(anyString(), anyString())).willReturn(Optional.of(medicalrecord));
		given(mockRepositoryFirestations.getStationsNumbersByAdress(anyString())).willReturn(listString);

		// WHEN
		fireDTO = service.getListPersonsByAdressFire(listAddresses.get(0));

		// THEN
		verify(mockRepositoryPersons, times(1)).getPersonsByAddress(anyString());
		verify(mockRepositoryMedicalrecords, times(1)).getBirthdateByFirstNameAndLastName(anyString(), anyString());
		verify(mockRepositoryMedicalrecords, times(1)).getMedicalrecordByFirstNameAndLastName(anyString(), anyString());
		verify(mockRepositoryFirestations, times(1)).getStationsNumbersByAdress(anyString());
		assertThat(fireDTO).isEqualTo(fireDTOAttendu);
	}

	@Test
	void testGivenStationNombersAre2And4_WhenGetListPersonsByStationNumbersFlood_ThenReturnFloodDTO() {
		// GIVEN
		List<FloodDTO> listFloodDTO = new ArrayList<>();
		List<FloodDTO> listFloodDTOAttendu = new ArrayList<>();
		FloodDTO floodDTO = new FloodDTO();
		List<String> stationNumbers = new ArrayList<>();

		stationNumbers.add("2");
		stationNumbers.add("4");

		floodDTO.setAddress(listAddresses.get(0));
		floodDTO.setPersons(listPersonsFireFloodDTO);
		listFloodDTOAttendu.add(floodDTO);

		given(mockRepositoryFirestations.getAddressesByStationNumber(anyString())).willReturn(listAddresses);
		given(mockRepositoryPersons.getPersonsByAddress(anyString())).willReturn(listPersons);
		given(mockRepositoryMedicalrecords.getBirthdateByFirstNameAndLastName(anyString(), anyString())).willReturn(Optional.of("02/04/2023"));
		given(mockRepositoryMedicalrecords.getMedicalrecordByFirstNameAndLastName(anyString(), anyString())).willReturn(Optional.of(medicalrecord));

		// WHEN
		listFloodDTO = service.getListPersonsByStationsNumbersFlood(stationNumbers);

		// THEN
		verify(mockRepositoryFirestations, times(2)).getAddressesByStationNumber(anyString());
		verify(mockRepositoryPersons, times(1)).getPersonsByAddress(anyString());
		verify(mockRepositoryMedicalrecords, times(1)).getBirthdateByFirstNameAndLastName(anyString(), anyString());
		verify(mockRepositoryMedicalrecords, times(1)).getMedicalrecordByFirstNameAndLastName(anyString(), anyString());
		assertThat(listFloodDTO).isEqualTo(listFloodDTOAttendu);
	}

	@Test
	void testGivenFirstNameIsPeterAndLastNameIsDuncan_WhenGetPersonInfoByFirstNameAndLastName_ThenReturnPersonInfoDTO() {
		// GIVEN
		List<PersonInfoDTO> listPersonInfoDTO = new ArrayList<>();
		List<PersonInfoDTO> listPersonInfoDTOAttendu = new ArrayList<>();
		PersonInfoDTO personInfoDTO = new PersonInfoDTO();

		personInfoDTO.setLastName(person.getLastName());
		personInfoDTO.setAddress(person.getAddress());
		personInfoDTO.setAge("0");
		personInfoDTO.setEmail(person.getEmail());
		personInfoDTO.setMedicalRecord(personMedicalRecordDTO);

		listPersonInfoDTOAttendu.add(personInfoDTO);

		given(mockRepositoryPersons.getPersonsByLastName(anyString())).willReturn(listPersons);
		given(mockRepositoryMedicalrecords.getMedicalrecordByFirstNameAndLastName(anyString(), anyString())).willReturn(Optional.of(medicalrecord));
		given(mockRepositoryMedicalrecords.getBirthdateByFirstNameAndLastName(anyString(), anyString())).willReturn(Optional.of("02/04/2023"));

		// WHEN
		listPersonInfoDTO = service.getPersonInfoByFirstNameAndLastName(listPersons.get(0).getFirstName(), listPersons.get(0).getLastName());

		// THEN
		verify(mockRepositoryPersons, times(1)).getPersonsByLastName(anyString());
		verify(mockRepositoryMedicalrecords, times(1)).getMedicalrecordByFirstNameAndLastName(anyString(), anyString());
		verify(mockRepositoryMedicalrecords, times(1)).getBirthdateByFirstNameAndLastName(anyString(), anyString());
		assertThat(listPersonInfoDTO).isEqualTo(listPersonInfoDTOAttendu);
	}

	@Test
	void testGivenFirstNameIsFosterAndLastNameIsShepard_WhenGetPerson_ThenReturnTrue() {
		// GIVEN
		Optional<Person> person = Optional.empty();
		Optional<Person> personAttendue = Optional.empty();

		given(mockRepositoryPersons.getPersonByFirstNameAndLastName(anyString(), anyString())).willReturn(personAttendue);

		// WHEN
		person = service.getPerson("Foster", "Shepard");

		// THEN
		verify(mockRepositoryPersons, times(1)).getPersonByFirstNameAndLastName(anyString(), anyString());
		assertThat(person).isEqualTo(personAttendue);
	}

	@Test
	void testGivenNothing_WhenGetAllPersons_ThenReturnTrue() {
		// GIVEN
		List<Person> listPersons = new ArrayList<>();
		List<Person> listPersonsAttendues = new ArrayList<>();

		given(mockRepositoryPersons.getAllPersons()).willReturn(listPersonsAttendues);

		// WHEN
		listPersons = service.getAllPersons();

		// THEN
		verify(mockRepositoryPersons, times(1)).getAllPersons();
		assertThat(listPersons).isEqualTo(listPersonsAttendues);
	}

	@Test
	void testGivenNewPerson_WhenSavePerson_ThenReturn1Person() throws ObjectAlreadyExistInDBException {
		// GIVEN
		Person p = new Person();

		given(mockRepositoryPersons.isExist(any(Person.class))).willReturn(false);
		given(mockRepositoryPersons.savePerson(any(Person.class))).willReturn(Optional.of(newPerson));

		// WHEN
		p = service.savePerson(newPerson).get();

		// THEN
		verify(mockRepositoryPersons, times(1)).isExist(any(Person.class));
		verify(mockRepositoryPersons, times(1)).savePerson(any(Person.class));
		assertThat(p).isEqualTo(newPerson);
	}

	@Test
	void testGivenNewPerson_WhenSavePersonAndProblemRepository_ThenReturnEmptyPerson() throws ObjectNotExistInDBException, ObjectAlreadyExistInDBException {
		// GIVEN
		Person person = null;

		given(mockRepositoryPersons.isExist(any(Person.class))).willReturn(false);
		given(mockRepositoryPersons.savePerson(any(Person.class))).willReturn(Optional.of(new Person()));

		// WHEN
		person = service.savePerson(newPerson).get();

		// THEN
		verify(mockRepositoryPersons, times(1)).isExist(any(Person.class));
		verify(mockRepositoryPersons, times(1)).savePerson(any(Person.class));
		assertThat(person).isEqualTo(new Person());
	}

	@Test
	void testGivenPersonAlreadyExist_WhenSavePerson_ThenThrowsObjectAlreadyExistInDBException() throws ObjectAlreadyExistInDBException {
		given(mockRepositoryPersons.isExist(any(Person.class))).willReturn(true);
		verify(mockRepositoryPersons, times(0)).savePerson(any(Person.class));
		assertThrows(ObjectAlreadyExistInDBException.class, () -> service.savePerson(person));
	}

	@Test
	void testGivenPerson_WhenUpdatePerson_ThenReturn1Person() throws ObjectNotExistInDBException {
		// GIVEN
		Person person = null;

		given(mockRepositoryPersons.getPersonByFirstNameAndLastName(anyString(), anyString())).willReturn(Optional.of(newPerson));
		given(mockRepositoryPersons.updatePerson(any(Person.class))).willReturn(Optional.of(modifiedPerson));

		// WHEN
		person = service.updatePerson(modifiedPerson).get();

		// THEN
		verify(mockRepositoryPersons, times(1)).getPersonByFirstNameAndLastName(anyString(), anyString());
		verify(mockRepositoryPersons, times(1)).updatePerson(any(Person.class));
		assertThat(person).isEqualTo(modifiedPerson);
	}

	@Test
	void testGivenPerson_WhenUpdatePersonAndProblemRepository_ThenReturnEmptyPerson() throws ObjectNotExistInDBException {
		// GIVEN
		Person person = null;

		given(mockRepositoryPersons.getPersonByFirstNameAndLastName(anyString(), anyString())).willReturn(Optional.of(newPerson));
		given(mockRepositoryPersons.updatePerson(any(Person.class))).willReturn(Optional.of(new Person()));

		// WHEN
		person = service.updatePerson(modifiedPerson).get();

		// THEN
		verify(mockRepositoryPersons, times(1)).getPersonByFirstNameAndLastName(anyString(), anyString());
		verify(mockRepositoryPersons, times(1)).updatePerson(any(Person.class));
		assertThat(person).isEqualTo(new Person());
	}

	@Test
	void testGivenUnknowPerson_WhenUpdatePerson_ThenThrowsObjectNotExistInDBException() throws ObjectNotExistInDBException {
		verify(mockRepositoryPersons, times(0)).updatePerson(any(Person.class));
		assertThrows(ObjectNotExistInDBException.class, () -> service.updatePerson(modifiedPerson));
	}

	@Test
	void testGivenPerson_WhenDeletePerson_ThenReturnTrue() throws ObjectNotExistInDBException {
		// GIVEN
		boolean result;

		given(mockRepositoryPersons.isExist(any(Person.class))).willReturn(true);
		given(mockRepositoryPersons.deletePerson(any(Person.class))).willReturn(true);

		// WHEN
		result = service.deletePerson(modifiedPerson);

		// THEN
		verify(mockRepositoryPersons, times(1)).isExist(any(Person.class));
		verify(mockRepositoryPersons, times(1)).deletePerson(any(Person.class));
		assertThat(result).isTrue();
	}

	@Test
	void testGivenPerson_WhenDeletePersonAndProblemRepository_ThenReturnFalse() throws ObjectNotExistInDBException {
		// GIVEN
		boolean result;

		given(mockRepositoryPersons.isExist(any(Person.class))).willReturn(true);
		given(mockRepositoryPersons.deletePerson(any(Person.class))).willReturn(false);

		// WHEN
		result = service.deletePerson(modifiedPerson);

		// THEN
		verify(mockRepositoryPersons, times(1)).isExist(any(Person.class));
		verify(mockRepositoryPersons, times(1)).deletePerson(any(Person.class));
		assertThat(result).isFalse();
	}

	@Test
	void testGivenPerson_WhenDeleteUnknowPerson_ThenThrowsObjectNotExistInDBException() throws ObjectNotExistInDBException {
		verify(mockRepositoryPersons, times(0)).deletePerson(any(Person.class));
		assertThrows(ObjectNotExistInDBException.class, () -> service.deletePerson(modifiedPerson));
	}

}
