package com.safetynetalert.api.repository;

import java.util.Set;

import org.springframework.stereotype.Repository;

@Repository
public interface IAddressesRepository {

	public Set<String> getPhonesByCity(final String city);

	public Set<String> getPhonesByFirestationNumber(final String firestationNumber);

	public Set<String> getEmailsByCity(final String city);

}
