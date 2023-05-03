package com.safetynetalert.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.safetynetalert.api.model.Person;
import com.safetynetalert.api.repository.impl.PersonsRepositoryFileImpl;

class PersonsRepositoryFileImplTest {

	private PersonsRepositoryFileImpl repository = new PersonsRepositoryFileImpl();
	private Optional<Person> optionalPerson;
	private List<Person> listPerson = new ArrayList<>();
	private List<Person> listPersonAttendues = new ArrayList<>();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		DataStatic.loadData("data.json");
}

	@BeforeEach
	void init() {
		DataStatic.resetDatas();
	}

	@Test
	void testGivenAddressIs112SteppesPl_WhenGetPersonsByAddress_ThenReturnListOf3Persons() {
		// GIVEN
		listPersonAttendues.add(DataStatic.getDatas().getPersons().get(9));
		listPersonAttendues.add(DataStatic.getDatas().getPersons().get(16));
		listPersonAttendues.add(DataStatic.getDatas().getPersons().get(17));

		// WHEN
		listPerson = repository.getPersonsByAddress("112 Steppes Pl");

		// THEN
		assertThat(listPerson).isEqualTo(listPersonAttendues);
	}

	@Test
	void testGivenAddressIs112JavaSteet_WhenGetPersonsByAddress_ThenReturn0Person() {
		// GIVEN

		// WHEN
		listPerson = repository.getPersonsByAddress("112 JavaSteet");

		// THEN
		assertThat(listPerson.size()).isEqualTo(0);
	}

	@Test
	void testGivenFirstNameIsTessaAndLastNameIsCarman_WhenGetPersonByFirstNameAndLastName_ThenReturn1Person() {
		// GIVEN
		optionalPerson = Optional.empty();

		// WHEN
		optionalPerson = repository.getPersonByFirstNameAndLastName("Tessa", "Carman");

		// THEN
		assertThat(optionalPerson).isEqualTo(Optional.of(DataStatic.getDatas().getPersons().get(6)));
	}

	@Test
	void testGivenFirstNameIsAlbertAndLastNameIsEinstein_WhenGetPersonByFirstNameAndLastName_ThenReturnPersonEmpty() {
		// GIVEN
		optionalPerson = Optional.empty();

		// WHEN
		optionalPerson = repository.getPersonByFirstNameAndLastName("Albert", "Einstein");

		// THEN
		assertThat(optionalPerson).isEqualTo(Optional.empty());
	}

	@Test
	void testGivenLastNameIsBoyd_WhenGetPersonsByLastName_ThenReturn6Persons() {
		// GIVEN
		listPerson.clear();
		listPersonAttendues.clear();

		listPersonAttendues.add(DataStatic.getDatas().getPersons().get(0));
		listPersonAttendues.add(DataStatic.getDatas().getPersons().get(1));
		listPersonAttendues.add(DataStatic.getDatas().getPersons().get(2));
		listPersonAttendues.add(DataStatic.getDatas().getPersons().get(3));
		listPersonAttendues.add(DataStatic.getDatas().getPersons().get(4));
		listPersonAttendues.add(DataStatic.getDatas().getPersons().get(17));

		// WHEN
		listPerson = repository.getPersonsByLastName("Boyd");

		// THEN
		assertThat(listPerson).isEqualTo(listPersonAttendues);
	}

	@Test
	void testGivenLastNameIsEinstein_WhenGetPersonsByLastName_ThenReturn0Person() {
		// GIVEN
		listPerson.clear();
		listPersonAttendues.clear();

		// WHEN
		listPerson = repository.getPersonsByLastName("Einstein");

		// THEN
		assertThat(listPerson).isEqualTo(listPersonAttendues);
	}

	@Test
	void testGivenNothing_WhenGetAllPersons_ThenReturn23Persons() {
		// GIVEN
		listPerson.clear();

		// WHEN
		listPerson = repository.getAllPersons();

		// THEN
		assertThat(listPerson.size()).isEqualTo(23);
		assertThat(listPerson.get(8)).isEqualTo(DataStatic.getDatas().getPersons().get(8));
	}

	@Test
	void testGivenNewPerson_WhenSavePerson_ThenReturn1Person() {
		// GIVEN
		Person person = new Person();
		Person personAttendue = new Person();

		personAttendue.setFirstName("Phil");
		personAttendue.setLastName("Finbert");
		personAttendue.setAddress("4 rue du Java");
		personAttendue.setCity("Poo");
		personAttendue.setZip("42");
		personAttendue.setPhone("06 07 08 09 01");
		personAttendue.setEmail("phil@java.com");

		// WHEN
		person = repository.savePerson(personAttendue).get();

		// THEN
		assertThat(person).isEqualTo(personAttendue);
		assertThat(DataStatic.getDatas().getPersons().size()).isEqualTo(24);
	}

	@Test
	void testGivenPerson_WhenUpdatePerson_ThenReturn1Person() {
		// GIVEN
		Person person = new Person();
		Person personOld;
		Person personAttendue;

		personOld = new Person(DataStatic.getDatas().getPersons().get(2));

		personAttendue = new Person(DataStatic.getDatas().getPersons().get(2));
		personAttendue.setAddress("1509 Culver St2");
		personAttendue.setCity("Culver2");
		personAttendue.setEmail("tenz@email2.com");

		// WHEN
		person = repository.updatePerson(personAttendue).get();

		// THEN
		assertThat(person).isEqualTo(personAttendue);
		assertThat(DataStatic.getDatas().getPersons().size()).isEqualTo(23);
		assertThat(DataStatic.getDatas().getPersons().contains(person)).isTrue();
		assertThat(DataStatic.getDatas().getPersons().contains(personOld)).isFalse();
	}

	@Test
	void testGivenPerson_WhenDeletePerson_ThenReturn22Persons() {
		// GIVEN
		Person personAttendue = DataStatic.getDatas().getPersons().get(2);

		// WHEN
		repository.deletePerson(personAttendue);

		// THEN
		assertThat(DataStatic.getDatas().getPersons().size()).isEqualTo(22);
		assertThat(DataStatic.getDatas().getPersons().contains(personAttendue)).isFalse();
	}

	@Test
	void testGivenPersonUnmodified_WhenIsExist_ThenReturnTrue() {
		// GIVEN
		boolean result = true;
		Person personAttendue = new Person(DataStatic.getDatas().getPersons().get(2));

		// WHEN
		result = repository.isExist(personAttendue);

		// THEN
		assertThat(result).isTrue();
	}

	@Test
	void testGivenUnknowPerson_WhenIsExist_ThenReturnFalse() {
		// GIVEN
		boolean result = false;
		Person personAttendue = new Person();

		personAttendue = new Person(DataStatic.getDatas().getPersons().get(2));
		personAttendue.setLastName("Boyd2");


		// WHEN
		result = repository.isExist(personAttendue);

		// THEN
		assertThat(result).isFalse();
	}

}
