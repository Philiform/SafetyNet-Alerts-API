package com.safetynetalert.api.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.safetynetalert.api.model.Medicalrecord;
import com.safetynetalert.api.repository.DataStatic;
import com.safetynetalert.api.repository.IMedicalrecordsRepository;

@Repository
public class MedicalrecordsRepositoryFileImpl implements IMedicalrecordsRepository {

	@Override
	public Optional<String> getBirthdateByFirstNameAndLastName(final String firstName, final String lastName) {
		for(Medicalrecord medicalrecord : DataStatic.getDatas().getMedicalrecords()) {
			if(medicalrecord.getFirstName().equals(firstName) && medicalrecord.getLastName().equals(lastName)) {
				return Optional.of(medicalrecord.getBirthdate());
			}
		}
		return Optional.empty();
	}
	@Override
	public Optional<Medicalrecord> getMedicalrecordByFirstNameAndLastName(final String firstName, final String lastName) {
		for(Medicalrecord medical : DataStatic.getDatas().getMedicalrecords()) {
			if(medical.getFirstName().equals(firstName) && medical.getLastName().equals(lastName)) {
				return Optional.of(medical);
			}
		}
		return Optional.empty();
	}
	@Override
	public List<Medicalrecord> getAllMedicalrecords() {
		return DataStatic.getDatas().getMedicalrecords();
	}
	@Override
	public Optional<Medicalrecord> saveMedicalrecord(final Medicalrecord medicalrecord) {
		if(DataStatic.getDatas().getMedicalrecords().add(medicalrecord)) {
			return Optional.of(medicalrecord);
		}

		return Optional.empty();
	}
	@Override
	public Optional<Medicalrecord> updateMedicalrecord(final Medicalrecord medicalrecord) {
		Optional<Medicalrecord> medicalrecordOld =  getMedicalrecordByFirstNameAndLastName(medicalrecord.getFirstName(), medicalrecord.getLastName());

		if(!medicalrecordOld.isEmpty()) {
			medicalrecordOld.get().setBirthdate(medicalrecord.getBirthdate());
			medicalrecordOld.get().setMedications(medicalrecord.getMedications());
			medicalrecordOld.get().setAllergies(medicalrecord.getAllergies());

			return medicalrecordOld;
		}

		return Optional.empty();
	}
	@Override
	public boolean deleteMedicalrecord(final Medicalrecord medicalrecord) {
		return DataStatic.getDatas().getMedicalrecords().remove(medicalrecord);
	}

	@Override
	public boolean isExist(final Medicalrecord medicalrecord) {
		return DataStatic.getDatas().getMedicalrecords().contains(medicalrecord);
	}

}
