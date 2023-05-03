package com.safetynetalert.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.safetynetalert.api.model.Firestation;

@Repository
public interface IFirestationsRepository {

	public List<String> getAddressesByStationNumber(final String stationNumber);

	public List<Firestation> getFirestationsByStationNumber(final String stationNumber);

	public List<String> getStationsNumbersByAdress(final String address);

	public List<Firestation> getFirestationsByAdress(final String address);

	public List<Firestation> getAllFirestations();

	public Optional<Firestation> saveFirestation(final Firestation firestation);

	public Optional<Firestation> updateFirestation(final String address, final String firestationNumber, final Firestation firestation);

	public boolean deleteFirestation(final Firestation firestation);

	public boolean isExist(final Firestation firestation);
}
