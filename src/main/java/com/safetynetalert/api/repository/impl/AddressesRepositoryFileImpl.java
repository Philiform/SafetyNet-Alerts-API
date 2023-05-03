package com.safetynetalert.api.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Repository;

import com.safetynetalert.api.model.Firestation;
import com.safetynetalert.api.model.Person;
import com.safetynetalert.api.repository.DataStatic;
import com.safetynetalert.api.repository.IAddressesRepository;

@Repository
public class AddressesRepositoryFileImpl implements IAddressesRepository {

	@Override
	public Set<String> getPhonesByCity(final String city) {
		Set<String> phones = new TreeSet<>();

		for(Person person : DataStatic.getDatas().getPersons()) {
			if(person.getCity().equals(city)) {
				phones.add(person.getPhone());
			}
		}
		return phones;
	}
	@Override
	public Set<String> getPhonesByFirestationNumber(final String firestationNumber) {
		Set<String> phones = new TreeSet<>();
		List<String> addresses = new ArrayList<>();

		for(Firestation firestation : DataStatic.getDatas().getFirestations()) {
			if(firestation.getStation().equals(firestationNumber)) {
				addresses.add(firestation.getAddress());
			}
		}

		for(Person person : DataStatic.getDatas().getPersons()) {
			if(addresses.contains(person.getAddress())) {
				phones.add(person.getPhone());
			}
		}
		return phones;
	}
	@Override
	public Set<String> getEmailsByCity(final String city) {
		Set<String> emails = new TreeSet<>();

		for(Person person : DataStatic.getDatas().getPersons()) {
			if(person.getCity().equals(city)) {
				emails.add(person.getEmail());
			}
		}
		return emails;
	}
}
