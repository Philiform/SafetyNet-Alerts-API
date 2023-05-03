package com.safetynetalert.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynetalert.api.exception.ObjectAlreadyExistInDBException;
import com.safetynetalert.api.exception.ObjectNotExistInDBException;
import com.safetynetalert.api.model.Firestation;
import com.safetynetalert.api.repository.IFirestationsRepository;

@Service
public class FirestationsService {

	@Autowired
	private IFirestationsRepository repository;

	public List<String> getAddressesByStationNumber(final String stationNumber) {
		if(!stationNumber.isEmpty()) {
			return repository.getAddressesByStationNumber(stationNumber);
		}
		return new ArrayList<>();
	}

	public List<Firestation> getFirestationsByStationNumber(final String stationNumber) {
		if(!stationNumber.isEmpty()) {
			return repository.getFirestationsByStationNumber(stationNumber);
		}
		return new ArrayList<>();
	}

	public List<String> getStationsNumbersByAdress(final String address) {
		if(!address.isEmpty()) {
			return repository.getStationsNumbersByAdress(address);
		}
		return new ArrayList<>();
	}

	public List<Firestation> getFirestationsByAdress(final String address) {
		if(!address.isEmpty()) {
			return repository.getFirestationsByAdress(address);
		}
		return new ArrayList<>();
	}

	public List<Firestation> getAllFirestations() {
		return repository.getAllFirestations();
	}

	public Optional<Firestation> saveFirestation(final Firestation firestation) throws ObjectAlreadyExistInDBException {
		if(repository.isExist(firestation)) {
			throw new ObjectAlreadyExistInDBException("It already exists.");
		}
		return repository.saveFirestation(firestation);
	}

	public Optional<Firestation> updateFirestation(final String address, final String firestationNumber, final Firestation firestation) throws ObjectNotExistInDBException {
		if(!repository.isExist(new Firestation(address, firestationNumber))) {
			throw new ObjectNotExistInDBException("It does not exist.");
		}
		return repository.updateFirestation(address, firestationNumber, firestation);
	}

	public boolean deleteFirestation(final Firestation firestation) throws ObjectNotExistInDBException {
		if(!repository.isExist(firestation)) {
			throw new ObjectNotExistInDBException("It does not exist.");
		}
		return repository.deleteFirestation(firestation);
	}
}
