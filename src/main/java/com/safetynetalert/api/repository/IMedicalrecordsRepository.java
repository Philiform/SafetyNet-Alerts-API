package com.safetynetalert.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.safetynetalert.api.model.Medicalrecord;

@Repository
public interface IMedicalrecordsRepository {

	public Optional<String> getBirthdateByFirstNameAndLastName(final String firstName, final String lastName);

	public Optional<Medicalrecord> getMedicalrecordByFirstNameAndLastName(final String firstName, final String lastName);

	public List<Medicalrecord> getAllMedicalrecords();

	public Optional<Medicalrecord> saveMedicalrecord(final Medicalrecord medicalrecord);

	public Optional<Medicalrecord> updateMedicalrecord(final Medicalrecord medicalrecord);

	public boolean deleteMedicalrecord(final Medicalrecord medicalrecord);

	public boolean isExist(final Medicalrecord medicalrecord);
}
