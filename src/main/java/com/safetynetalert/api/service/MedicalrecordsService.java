package com.safetynetalert.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynetalert.api.exception.DateFutureException;
import com.safetynetalert.api.exception.ObjectAlreadyExistInDBException;
import com.safetynetalert.api.exception.ObjectNotExistInDBException;
import com.safetynetalert.api.model.Medicalrecord;
import com.safetynetalert.api.repository.IMedicalrecordsRepository;

@Service
public class MedicalrecordsService {

	@Autowired
	private IMedicalrecordsRepository repository;

	public Optional<String> getBirthdateByFirstNameAndLastName(final String firstName, final String lastName) {
		if(!firstName.isEmpty() && !lastName.isEmpty()) {
			return repository.getBirthdateByFirstNameAndLastName(firstName, lastName);
		}
		return Optional.empty();
	}

	public Optional<Medicalrecord> getMedicalrecordByFirstNameAndLastName(final String firstName, final String lastName) {
		if(!firstName.isEmpty() && !lastName.isEmpty()) {
			return repository.getMedicalrecordByFirstNameAndLastName(firstName, lastName);
		}
		return Optional.empty();
	}

	public List<Medicalrecord> getAllMedicalrecords() {
		return repository.getAllMedicalrecords();
	}

	public Optional<Medicalrecord> saveMedicalrecord(final Medicalrecord medicalrecord) throws ObjectAlreadyExistInDBException {
		if(repository.isExist(medicalrecord)) {
			throw new ObjectAlreadyExistInDBException("It already exists.");
		}
		return repository.saveMedicalrecord(medicalrecord);
	}

	public Optional<Medicalrecord> updateMedicalrecord(final Medicalrecord medicalrecord) throws ObjectNotExistInDBException {
		if(getMedicalrecordByFirstNameAndLastName(medicalrecord.getFirstName(), medicalrecord.getLastName()).isEmpty()) {
			throw new ObjectNotExistInDBException("It does not exist.");
		}
		return repository.updateMedicalrecord(medicalrecord);
	}

	public boolean deleteMedicalrecord(final Medicalrecord medicalrecord) throws ObjectNotExistInDBException {
		if(!repository.isExist(medicalrecord)) {
			throw new ObjectNotExistInDBException("It does not exist.");
		}
		return repository.deleteMedicalrecord(medicalrecord);
	}

	public boolean isBirthdateValid(final String birthdate) throws DateTimeParseException, DateFutureException {
		LocalDate date;
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		try {
			date = LocalDate.parse(birthdate, formatter);

			if(date.isBefore(today.plusDays(1))) {
				return true;
			} else {
				throw new DateFutureException("must be today or in the past");
			}
		} catch (DateTimeParseException ex) {
			throw ex;
		}
	}

}
