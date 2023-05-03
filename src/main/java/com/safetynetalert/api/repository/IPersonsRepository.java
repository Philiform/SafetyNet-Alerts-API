package com.safetynetalert.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.safetynetalert.api.model.Person;

@Repository
public interface IPersonsRepository {

	public List<Person> getPersonsByAddress(final String address);

	public Optional<Person> getPersonByFirstNameAndLastName(final String firstName, final String lastName);

	public List<Person> getPersonsByLastName(final String lastName );

	public List<Person> getAllPersons();

	public Optional<Person> savePerson(final Person person);

	public Optional<Person> updatePerson(final Person person);

	public boolean deletePerson(final Person person);

	public boolean isExist(final Person person);
}
