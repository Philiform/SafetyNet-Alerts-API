package com.safetynetalert.api.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.safetynetalert.api.model.Person;
import com.safetynetalert.api.repository.DataStatic;
import com.safetynetalert.api.repository.IPersonsRepository;

@Repository
public class PersonsRepositoryFileImpl implements IPersonsRepository {

	@Override
	public List<Person> getPersonsByAddress(final String address) {
		List<Person> persons = new ArrayList<>();

		for(Person person : DataStatic.getDatas().getPersons()) {
			if(person.getAddress().equals(address)) {
				persons.add(person);
			}
		}
		return persons;
	}
	@Override
	public Optional<Person> getPersonByFirstNameAndLastName(final String firstName, final String lastName) {
		for(Person p : DataStatic.getDatas().getPersons()) {
			if(p.getFirstName().equals(firstName) &&
				p.getLastName().equals(lastName)) {
				return Optional.of(p);
			}
		}
		return Optional.empty();
	}
	@Override
	public List<Person> getPersonsByLastName(final String lastName) {
		List<Person> listPersons = new ArrayList<>();
		for(Person p : DataStatic.getDatas().getPersons()) {
			if(p.getLastName().equals(lastName)) {
				listPersons.add(p);
			}
		}
		return listPersons;
	}
	@Override
	public List<Person> getAllPersons() {
		return DataStatic.getDatas().getPersons();
	}
	@Override
	public Optional<Person> savePerson(final Person person) {
		if(DataStatic.getDatas().getPersons().add(person)) {
			return Optional.of(person);
		}

		return Optional.empty();
	}
	@Override
	public Optional<Person> updatePerson(final Person person) {
		Optional<Person> personOld =  getPersonByFirstNameAndLastName(person.getFirstName(), person.getLastName());

		if(!personOld.isEmpty()) {
			personOld.get().setAddress(person.getAddress());
			personOld.get().setCity(person.getCity());
			personOld.get().setZip(person.getZip());
			personOld.get().setPhone(person.getPhone());
			personOld.get().setEmail(person.getEmail());

			return personOld;
		}

		return Optional.empty();
	}
	@Override
	public boolean deletePerson(final Person person) {
		return DataStatic.getDatas().getPersons().remove(person);
	}
	@Override
	public boolean isExist(final Person person) {
		return DataStatic.getDatas().getPersons().contains(person);
	}

}
