package com.safetynetalert.api.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.safetynetalert.api.model.Firestation;
import com.safetynetalert.api.repository.DataStatic;
import com.safetynetalert.api.repository.IFirestationsRepository;

@Repository
public class FirestationsRepositoryFileImpl implements IFirestationsRepository {

	@Override
	public List<String> getAddressesByStationNumber(final String stationNumber) {
		List<String> addressesStations = new ArrayList<>();

		for(Firestation firestation : DataStatic.getDatas().getFirestations()) {
			if(firestation.getStation().equals(stationNumber)) {
				addressesStations.add(firestation.getAddress());
			}
		}
		return addressesStations;
	}
	@Override
	public List<Firestation> getFirestationsByStationNumber(final String stationNumber) {
		List<Firestation> list = new ArrayList<>();

		for(Firestation firestation : DataStatic.getDatas().getFirestations()) {
			if(firestation.getStation().equals(stationNumber)) {
				list.add(firestation);
			}
		}
		return list;
	}
	@Override
	public List<String> getStationsNumbersByAdress(final String address) {
		List<String> list = new ArrayList<>();

		for(Firestation firestation : DataStatic.getDatas().getFirestations()) {
			if(firestation.getAddress().equals(address)) {
				list.add(firestation.getStation());
			}
		}
		return list;
	}
	@Override
	public List<Firestation> getFirestationsByAdress(final String address) {
		List<Firestation> list = new ArrayList<>();

		for(Firestation firestation : DataStatic.getDatas().getFirestations()) {
			if(firestation.getAddress().equals(address)) {
				list.add(firestation);
			}
		}
		return list;
	}
	@Override
	public List<Firestation> getAllFirestations() {
		return DataStatic.getDatas().getFirestations();
	}
	@Override
	public Optional<Firestation> saveFirestation(final Firestation firestation) {
		if(DataStatic.getDatas().getFirestations().add(firestation)) {
			return Optional.of(firestation);
		}

		return Optional.empty();
	}
	@Override
	public Optional<Firestation> updateFirestation(final String address, final String firestationNumber, final Firestation firestation) {
		int index = DataStatic.getDatas().getFirestations().indexOf(new Firestation(address, firestationNumber));
		Firestation f = DataStatic.getDatas().getFirestations().get(index);
		f.setStation(firestation.getStation());

		return Optional.of(f);
	}
	@Override
	public boolean deleteFirestation(final Firestation firestation) {
		return DataStatic.getDatas().getFirestations().remove(firestation);
	}
	@Override
	public boolean isExist(final Firestation firestation) {
		return DataStatic.getDatas().getFirestations().contains(firestation);
	}
}
