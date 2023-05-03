package com.safetynetalert.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynetalert.api.exception.DateFutureException;
import com.safetynetalert.api.exception.ObjectAlreadyExistInDBException;
import com.safetynetalert.api.exception.ObjectNotExistInDBException;
import com.safetynetalert.api.model.Medicalrecord;
import com.safetynetalert.api.repository.IMedicalrecordsRepository;

@ExtendWith(MockitoExtension.class)
class MedicalrecordsServiceTest {

	@InjectMocks
	private MedicalrecordsService service;

	@Mock
	private IMedicalrecordsRepository mockRepository;

	private static Medicalrecord medicalrecord;
	private static Medicalrecord newMedicalrecord;
	private static Medicalrecord modifiedMedicalrecord;
	private static Medicalrecord medicalrecordBirthdateTomorrow;
	private static Medicalrecord medicalrecordBirthdateDayIs33;
	private static List<Medicalrecord> listMedicalrecords = new ArrayList<>();
	private static Optional<String> string;
	private static Optional<String> stringAttendue;
	private static List<String> listString = new ArrayList<>();
	private static List<String> listStringAttendues = new ArrayList<>();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		medicalrecord = new Medicalrecord();
		medicalrecord.setFirstName("Clive");
		medicalrecord.setLastName("Ferguson");
		medicalrecord.setBirthdate("03/06/1994");
		medicalrecord.setMedications(listString);
		medicalrecord.setAllergies(listString);

		newMedicalrecord = new Medicalrecord();
		newMedicalrecord.setFirstName("Phil");
		newMedicalrecord.setLastName("Finbert");
		newMedicalrecord.setBirthdate("03/04/2023");
		newMedicalrecord.setMedications(listString);
		newMedicalrecord.setAllergies(listString);

		modifiedMedicalrecord = new Medicalrecord();
		modifiedMedicalrecord.setFirstName("Phil");
		modifiedMedicalrecord.setLastName("Finbert");
		modifiedMedicalrecord.setBirthdate("03/04/2020");
		modifiedMedicalrecord.setMedications(listString);
		modifiedMedicalrecord.setAllergies(listString);

		medicalrecordBirthdateTomorrow = new Medicalrecord();
		medicalrecordBirthdateTomorrow.setFirstName("Phil");
		medicalrecordBirthdateTomorrow.setLastName("Finbert");
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		medicalrecordBirthdateTomorrow.setBirthdate(today.plusDays(1).format(formatter).toString());
		medicalrecordBirthdateTomorrow.setMedications(listString);
		medicalrecordBirthdateTomorrow.setAllergies(listString);

		medicalrecordBirthdateDayIs33 = new Medicalrecord();
		medicalrecordBirthdateDayIs33.setFirstName("Phil");
		medicalrecordBirthdateDayIs33.setLastName("Finbert");
		medicalrecordBirthdateDayIs33.setBirthdate("02/33/2023");
		medicalrecordBirthdateDayIs33.setMedications(listString);
		medicalrecordBirthdateDayIs33.setAllergies(listString);
}

	@Test
	void testGivenFirstNameIsEricAndlastNameIsCadigan_WhenGetBirthdateByFirstNameAndLastName_ThenReturnTrue() {
		// GIVEN
		string = Optional.empty();
		stringAttendue = Optional.empty();

		given(mockRepository.getBirthdateByFirstNameAndLastName(anyString(), anyString())).willReturn(stringAttendue);

		// WHEN
		string = service.getBirthdateByFirstNameAndLastName("Eric", "Cadigan");

		// THEN
		verify(mockRepository, times(1)).getBirthdateByFirstNameAndLastName(anyString(), anyString());
		assertThat(string.equals(stringAttendue)).isTrue();
	}

	@Test
	void testGivenFirstNameIsEmptyAndlastNameIsCadigan_WhenGetBirthdateByFirstNameAndLastName_ThenReturnEmpty() {
		// GIVEN
		// WHEN
		string = service.getBirthdateByFirstNameAndLastName("", "Cadigan");

		// THEN
		verify(mockRepository, times(0)).getBirthdateByFirstNameAndLastName(anyString(), anyString());
		assertThat(string).isEmpty();
	}

	@Test
	void testGivenFirstNameIsEricAndlastNameIsEmpty_WhenGetBirthdateByFirstNameAndLastName_ThenReturnEmpty() {
		// GIVEN
		// WHEN
		string = service.getBirthdateByFirstNameAndLastName("Eric", "");

		// THEN
		verify(mockRepository, times(0)).getBirthdateByFirstNameAndLastName(anyString(), anyString());
		assertThat(string).isEmpty();
	}

	@Test
	void testGivenFirstNameIsEmptyAndlastNameIsEmpty_WhenGetBirthdateByFirstNameAndLastName_ThenReturnEmpty() {
		// GIVEN
		// WHEN
		string = service.getBirthdateByFirstNameAndLastName("", "");

		// THEN
		verify(mockRepository, times(0)).getBirthdateByFirstNameAndLastName(anyString(), anyString());
		assertThat(string).isEmpty();
	}

	@Test
	void testGivenFirstNameIsCliveAndlastNameIsFerguson_WhenGetMedicalrecordByFirstNameAndLastName_ThenReturnMedicalrecord() {
		// GIVEN
		Optional<Medicalrecord> m = Optional.empty();

		given(mockRepository.getMedicalrecordByFirstNameAndLastName(anyString(), anyString())).willReturn(Optional.of(medicalrecord));

		// WHEN
		m = service.getMedicalrecordByFirstNameAndLastName("Clive", "Ferguson");

		// THEN
		verify(mockRepository, times(1)).getMedicalrecordByFirstNameAndLastName(anyString(), anyString());
		assertThat(m.equals(Optional.of(medicalrecord))).isTrue();
	}

	@Test
	void testGivenFirstNameIsEmptyAndlastNameIsFerguson_WhenGetMedicalrecordByFirstNameAndLastName_ThenReturnEmpty() {
		// WHEN
		Optional<Medicalrecord> m = service.getMedicalrecordByFirstNameAndLastName("", "Ferguson");

		// THEN
		verify(mockRepository, times(0)).getMedicalrecordByFirstNameAndLastName(anyString(), anyString());
		assertThat(m).isEmpty();
	}

	@Test
	void testGivenFirstNameIsCliveAndlastNameIsEmpty_WhenGetMedicalrecordByFirstNameAndLastName_ThenReturnEmpty() {
		// WHEN
		Optional<Medicalrecord> m = service.getMedicalrecordByFirstNameAndLastName("Clive", "");

		// THEN
		verify(mockRepository, times(0)).getMedicalrecordByFirstNameAndLastName(anyString(), anyString());
		assertThat(m).isEmpty();
	}

	@Test
	void testGivenFirstNameIsEmptyAndlastNameIsEmpty_WhenGetMedicalrecordByFirstNameAndLastName_ThenReturnEmpty() {
		// WHEN
		Optional<Medicalrecord> m = service.getMedicalrecordByFirstNameAndLastName("", "");

		// THEN
		verify(mockRepository, times(0)).getMedicalrecordByFirstNameAndLastName(anyString(), anyString());
		assertThat(m).isEmpty();
	}

	@Test
	void testGivenNothing_WhenGetAllMedicalrecords_ThenReturnTrue() {
		// GIVEN
		listMedicalrecords.clear();

		given(mockRepository.getAllMedicalrecords()).willReturn(listMedicalrecords);

		// WHEN
		listMedicalrecords = service.getAllMedicalrecords();

		// THEN
		verify(mockRepository, times(1)).getAllMedicalrecords();
		assertThat(listString.equals(listStringAttendues)).isTrue();
	}

	@Test
	void testGivenNewMedicalrecord_WhenSaveMedicalrecord_ThenReturn1Medicalrecord() throws ObjectAlreadyExistInDBException {
		// GIVEN
		Medicalrecord medicalrecord = new Medicalrecord();

		given(mockRepository.isExist(any(Medicalrecord.class))).willReturn(false);
		given(mockRepository.saveMedicalrecord(any(Medicalrecord.class))).willReturn(Optional.of(newMedicalrecord));

		// WHEN
		medicalrecord = service.saveMedicalrecord(newMedicalrecord).get();

		// THEN
		verify(mockRepository, times(1)).isExist(any(Medicalrecord.class));
		verify(mockRepository, times(1)).saveMedicalrecord(any(Medicalrecord.class));
		assertThat(medicalrecord.equals(newMedicalrecord)).isTrue();
	}

	@Test
	void testGivenNewMedicalrecordAndProblemRepository_WhenSaveMedicalrecord_ThenReturnEmptyMedicalrecord() throws ObjectAlreadyExistInDBException {
		// GIVEN
		Medicalrecord medicalrecord = new Medicalrecord();

		given(mockRepository.isExist(any(Medicalrecord.class))).willReturn(false);
		given(mockRepository.saveMedicalrecord(any(Medicalrecord.class))).willReturn(Optional.of(new Medicalrecord()));

		// WHEN
		medicalrecord = service.saveMedicalrecord(newMedicalrecord).get();

		// THEN
		verify(mockRepository, times(1)).isExist(any(Medicalrecord.class));
		verify(mockRepository, times(1)).saveMedicalrecord(any(Medicalrecord.class));
		assertThat(medicalrecord).isEqualTo(new Medicalrecord());
	}

	@Test
	void testGivenMedicalrecordAlreadyExist_WhenSavePerson_ThenThrowsObjectAlreadyExistInDBException() throws ObjectAlreadyExistInDBException {
		given(mockRepository.isExist(any(Medicalrecord.class))).willReturn(true);
		verify(mockRepository, times(0)).saveMedicalrecord(any(Medicalrecord.class));
		assertThrows(ObjectAlreadyExistInDBException.class, () -> service.saveMedicalrecord(medicalrecord));
	}

	@Test
	void testGivenMedicalrecord_WhenUpdateMedicalrecord_ThenReturn1Medicalrecord() throws ObjectNotExistInDBException {
		// GIVEN
		Medicalrecord medicalrecord = null;

		given(mockRepository.getMedicalrecordByFirstNameAndLastName(anyString(), anyString())).willReturn(Optional.of(newMedicalrecord));
		given(mockRepository.updateMedicalrecord(any(Medicalrecord.class))).willReturn(Optional.of(modifiedMedicalrecord));

		// WHEN
		medicalrecord = service.updateMedicalrecord(modifiedMedicalrecord).get();

		// THEN
		verify(mockRepository, times(1)).getMedicalrecordByFirstNameAndLastName(anyString(), anyString());
		verify(mockRepository, times(1)).updateMedicalrecord(any(Medicalrecord.class));
		assertThat(medicalrecord).isEqualTo(modifiedMedicalrecord);
	}

	@Test
	void testGivenMedicalrecord_WhenUpdateMedicalrecordAndProblemRepository_ThenReturnEmptyMedicalrecord() throws ObjectNotExistInDBException {
		// GIVEN
		Medicalrecord medicalrecord = new Medicalrecord();

		given(mockRepository.getMedicalrecordByFirstNameAndLastName(anyString(), anyString())).willReturn(Optional.of(newMedicalrecord));
		given(mockRepository.updateMedicalrecord(any(Medicalrecord.class))).willReturn(Optional.of(new Medicalrecord()));

		// WHEN
		medicalrecord = service.updateMedicalrecord(newMedicalrecord).get();

		// THEN
		verify(mockRepository, times(1)).getMedicalrecordByFirstNameAndLastName(anyString(), anyString());
		verify(mockRepository, times(1)).updateMedicalrecord(any(Medicalrecord.class));
		assertThat(medicalrecord).isEqualTo(new Medicalrecord());
	}

	@Test
	void testGivenUnknowMedicalrecord_WhenUpdateMedicalrecord_ThenThrowsObjectNotExistInDBException() throws ObjectNotExistInDBException {
		verify(mockRepository, times(0)).updateMedicalrecord(any(Medicalrecord.class));
		assertThrows(ObjectNotExistInDBException.class, () -> service.updateMedicalrecord(modifiedMedicalrecord));
	}

	@Test
	void testGivenMedicalrecord_WhenDeleteMedicalrecord_ThenReturnTrue() throws ObjectNotExistInDBException {
		// GIVEN
		boolean result;

		given(mockRepository.isExist(any(Medicalrecord.class))).willReturn(true);
		given(mockRepository.deleteMedicalrecord(any(Medicalrecord.class))).willReturn(true);

		// WHEN
		result = service.deleteMedicalrecord(modifiedMedicalrecord);

		// THEN
		verify(mockRepository, times(1)).isExist(any(Medicalrecord.class));
		verify(mockRepository, times(1)).deleteMedicalrecord(any(Medicalrecord.class));
		assertThat(result).isTrue();
	}

	@Test
	void testGivenMedicalrecord_WhenDeleteMedicalrecordAndProblemRepository_ThenReturnFalse() throws ObjectNotExistInDBException {
		// GIVEN
		boolean result;

		given(mockRepository.isExist(any(Medicalrecord.class))).willReturn(true);
		given(mockRepository.deleteMedicalrecord(any(Medicalrecord.class))).willReturn(false);

		// WHEN
		result = service.deleteMedicalrecord(modifiedMedicalrecord);

		// THEN
		verify(mockRepository, times(1)).isExist(any(Medicalrecord.class));
		verify(mockRepository, times(1)).deleteMedicalrecord(any(Medicalrecord.class));
		assertThat(result).isFalse();
	}

	@Test
	void testGivenMedicalrecord_WhenDeleteUnknowMedicalrecord_ThenThrowsObjectNotExistInDBException() throws ObjectNotExistInDBException {
		verify(mockRepository, times(0)).deleteMedicalrecord(any(Medicalrecord.class));
		assertThrows(ObjectNotExistInDBException.class, () -> service.deleteMedicalrecord(modifiedMedicalrecord));
	}

	@Test
	public void testGivenMedicalrecordWithBirthDate_WhenIsBirthdateValid_ThenReturnTrue() throws DateTimeParseException, DateFutureException {
		boolean result = service.isBirthdateValid(medicalrecord.getBirthdate());
		assertThat(result).isTrue();
	}

	@Test
	public void testGivenMedicalrecordWithTomorrowBirthDate_WhenIsBirthdateValid_ThenThrowsDateFutureException() throws DateTimeParseException, DateFutureException {
		assertThrows(DateFutureException.class, () -> service.isBirthdateValid(medicalrecordBirthdateTomorrow.getBirthdate()));
	}

	@Test
	public void testGivenMedicalrecordWithBirthDateDayIs33_WhenSaveMedicalrecord_ThenThrowsDateTimeParseException() {
		assertThrows(DateTimeParseException.class, () -> service.isBirthdateValid(medicalrecordBirthdateDayIs33.getBirthdate()));
	}

}
