package com.safetynetalert.api.service;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynetalert.api.repository.IAddressesRepository;

@Service
public class AddressesService {

	@Autowired
	private IAddressesRepository repository;

	public Set<String> getPhonesByCity(final String city) {
		if(!city.isEmpty()) {
			return repository.getPhonesByCity(city);
		}
		return new TreeSet<>();
	}

	public Set<String> getPhonesByFirestationNumber(final String firestationNumber) {
		if(!firestationNumber.isEmpty()) {
			return repository.getPhonesByFirestationNumber(firestationNumber);
		}
		return new TreeSet<>();
	}

	public Set<String> getEmailsByCity(final String city) {
		if(!city.isEmpty()) {
			return repository.getEmailsByCity(city);
		}
		return new TreeSet<>();
	}

}
