package com.safetynetalert.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynetalert.api.exception.ObjectAlreadyExistInDBException;
import com.safetynetalert.api.exception.ObjectNotExistInDBException;
import com.safetynetalert.api.model.Firestation;
import com.safetynetalert.api.repository.IFirestationsRepository;

@ExtendWith(MockitoExtension.class)
class FirestationsServiceTest {

	@InjectMocks
	private FirestationsService service;

	@Mock
	private IFirestationsRepository mockRepository;

	private static Firestation firestation;
	private static Firestation newFirestation;
	private static Firestation modifiedFirestation;
	private static List<Firestation> listFirestations;
	private static List<Firestation> listFirestationsAttendues;
	private static List<String> listString;
	private static List<String> listStringAttendues;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		firestation = new Firestation();
		firestation.setAddress("112 Steppes Pl");
		firestation.setStation("4");

		newFirestation = new Firestation();
		newFirestation.setAddress("200 Java Street");
		newFirestation.setStation("4");

		modifiedFirestation = new Firestation();
		modifiedFirestation.setAddress("112 Steppes Pl");
		modifiedFirestation.setStation("8");


		listFirestations = new ArrayList<>();

		listFirestationsAttendues = new ArrayList<>();
		listFirestationsAttendues.add(firestation);
		listFirestationsAttendues.add(newFirestation);

		listString = new ArrayList<>();

		listStringAttendues = new ArrayList<>();
		listStringAttendues.add("112 Steppes Pl");
		listStringAttendues.add("748 Townings Dr");
	}

	@Test
	void testGivenStationIs2_WhenGetAddressesByStationNumber_ThenReturnTrue() {
		// GIVEN
		listString.clear();

		given(mockRepository.getAddressesByStationNumber(anyString())).willReturn(listStringAttendues);

		// WHEN
		listString = service.getAddressesByStationNumber("2");

		// THEN
		verify(mockRepository, times(1)).getAddressesByStationNumber(anyString());
		assertThat(listString.equals(listStringAttendues)).isTrue();
	}

	@Test
	void testGivenStationIsEmpty_WhenGetAddressesByStationNumber_ThenReturnEmpty() {
		// WHEN
		listString = service.getAddressesByStationNumber("");

		// THEN
		verify(mockRepository, times(0)).getAddressesByStationNumber(anyString());
		assertThat(listString).isEmpty();
	}

	@Test
	void testGivenStationIs2_WhenGetFirestationsByStationNumber_ThenReturnTrue() {
		// GIVEN
		listFirestations.clear();

		given(mockRepository.getFirestationsByStationNumber(anyString())).willReturn(listFirestationsAttendues);

		// WHEN
		listFirestations = service.getFirestationsByStationNumber("2");

		// THEN
		verify(mockRepository, times(1)).getFirestationsByStationNumber(anyString());
		assertThat(listFirestations.equals(listFirestationsAttendues)).isTrue();
	}

	@Test
	void testGivenStationIsEmpty_WhenGetFirestationsByStationNumber_ThenReturnEmpty() {
		// WHEN
		listFirestations = service.getFirestationsByStationNumber("");

		// THEN
		verify(mockRepository, times(0)).getFirestationsByStationNumber(anyString());
		assertThat(listFirestations).isEmpty();
	}

	@Test
	void testGivenAddresStationIs112SteppesPl_WhenGetStationsNumbersByAdress_ThenReturnTrue() {
		// GIVEN
		listString.clear();

		given(mockRepository.getStationsNumbersByAdress(anyString())).willReturn(listStringAttendues);

		// WHEN
		listString = service.getStationsNumbersByAdress("2");

		// THEN
		verify(mockRepository, times(1)).getStationsNumbersByAdress(anyString());
		assertThat(listString.equals(listStringAttendues)).isTrue();
	}

	@Test
	void testGivenAddresStationIsEmpty_WhenGetStationsNumbersByAdress_ThenReturnEmpty() {
		// WHEN
		listString = service.getStationsNumbersByAdress("");

		// THEN
		verify(mockRepository, times(0)).getStationsNumbersByAdress(anyString());
		assertThat(listString).isEmpty();
	}

	@Test
	void testGivenAddresStationIs112SteppesPl_WhenGetFirestationsByAdress_ThenReturnTrue() {
		// GIVEN
		listFirestations.clear();

		given(mockRepository.getFirestationsByAdress(anyString())).willReturn(listFirestationsAttendues);

		// WHEN
		listFirestations = service.getFirestationsByAdress("112 Steppes Pl");

		// THEN
		verify(mockRepository, times(1)).getFirestationsByAdress(anyString());
		assertThat(listFirestations.equals(listFirestationsAttendues)).isTrue();
	}

	@Test
	void testGivenAddresStationIsEmpty_WhenGetFirestationsByAdress_ThenReturnEmpty() {
		// WHEN
		listFirestations = service.getFirestationsByAdress("");

		// THEN
		verify(mockRepository, times(0)).getFirestationsByAdress(anyString());
		assertThat(listFirestations).isEmpty();
	}

	@Test
	void testGivenNothing_WhenGetAllFirestations_ThenReturnTrue() {
		// GIVEN
		listFirestations.clear();

		given(mockRepository.getAllFirestations()).willReturn(listFirestationsAttendues);

		// WHEN
		listFirestations = service.getAllFirestations();

		// THEN
		verify(mockRepository, times(1)).getAllFirestations();
		assertThat(listFirestations.equals(listFirestationsAttendues)).isTrue();
	}

	@Test
	void testGivenNewFirestation_WhenSaveFirestation_ThenReturnTrue() throws ObjectAlreadyExistInDBException {
		// GIVEN
		Firestation f = new Firestation();

		given(mockRepository.isExist(any(Firestation.class))).willReturn(false);
		given(mockRepository.saveFirestation(any(Firestation.class))).willReturn(Optional.of(newFirestation));

		// WHEN
		f = service.saveFirestation(newFirestation).get();

		// THEN
		verify(mockRepository, times(1)).isExist(any(Firestation.class));
		verify(mockRepository, times(1)).saveFirestation(any(Firestation.class));
		assertThat(f.equals(newFirestation)).isTrue();
	}

	@Test
	void testGivenNewFirestationAndProblemRepository_WhenSaveFirestation_ThenReturnEmptyFirestation() throws ObjectAlreadyExistInDBException {
		// GIVEN
		// GIVEN
		Firestation f = new Firestation();

		given(mockRepository.isExist(any(Firestation.class))).willReturn(false);
		given(mockRepository.saveFirestation(any(Firestation.class))).willReturn(Optional.of(new Firestation()));

		// WHEN
		f = service.saveFirestation(newFirestation).get();

		// THEN
		verify(mockRepository, times(1)).isExist(any(Firestation.class));
		verify(mockRepository, times(1)).saveFirestation(any(Firestation.class));
		assertThat(f).isEqualTo(new Firestation());
	}

	@Test
	void testGivenFirestationAlreadyExist_WhenSaveFirestation_ThenThrowsObjectAlreadyExistInDBException() {
		given(mockRepository.isExist(any(Firestation.class))).willReturn(true);
		verify(mockRepository, times(0)).saveFirestation(any(Firestation.class));
		assertThrows(ObjectAlreadyExistInDBException.class, () -> service.saveFirestation(firestation));
	}

	@Test
	void testGivenFirestation_WhenUpdateFirestation_ThenReturn1Firestation() throws ObjectNotExistInDBException {
		// GIVEN
		Firestation firestation = null;

		given(mockRepository.isExist(any(Firestation.class))).willReturn(true);
		given(mockRepository.updateFirestation(anyString(), anyString(), any(Firestation.class))).willReturn(Optional.of(modifiedFirestation));

		// WHEN
		firestation = service.updateFirestation("112 Steppes Pl", "4", modifiedFirestation).get();

		// THEN
		verify(mockRepository, times(1)).isExist(any(Firestation.class));
		verify(mockRepository, times(1)).updateFirestation(anyString(), anyString(), any(Firestation.class));
		assertThat(firestation).isEqualTo(modifiedFirestation);
	}

	@Test
	void testGivenFirestation_WhenUpdateFirestationAndProblemRepository_ThenReturnEmptyFirestation() throws ObjectNotExistInDBException {
		// GIVEN
		Firestation firestation = new Firestation();

		given(mockRepository.isExist(any(Firestation.class))).willReturn(true);
		given(mockRepository.updateFirestation(anyString(), anyString(), any(Firestation.class))).willReturn(Optional.of(new Firestation()));

		// WHEN
		firestation = service.updateFirestation("112 Steppes Pl", "4", modifiedFirestation).get();

		// THEN
		verify(mockRepository, times(1)).isExist(any(Firestation.class));
		verify(mockRepository, times(1)).updateFirestation(anyString(), anyString(), any(Firestation.class));
		assertThat(firestation).isEqualTo(new Firestation());
	}

	@Test
	void testGivenUnknowFirestation_WhenUpdateFirestation_ThenThrowsObjectNotExistInDBException() throws ObjectNotExistInDBException {
		verify(mockRepository, times(0)).updateFirestation(anyString(), anyString(), any(Firestation.class));
		assertThrows(ObjectNotExistInDBException.class, () -> service.updateFirestation("112 Steppes Pl", "4", modifiedFirestation));
	}

	@Test
	void testGivenFirestation_WhenDeleteFirestation_ThenReturnTrue() throws ObjectNotExistInDBException {
		// GIVEN
		boolean result;

		given(mockRepository.isExist(any(Firestation.class))).willReturn(true);
		given(mockRepository.deleteFirestation(any(Firestation.class))).willReturn(true);

		// WHEN
		result = service.deleteFirestation(modifiedFirestation);

		// THEN
		verify(mockRepository, times(1)).isExist(any(Firestation.class));
		verify(mockRepository, times(1)).deleteFirestation(any(Firestation.class));
		assertThat(result).isTrue();
	}

	@Test
	void testGivenFirestation_WhenDeleteFirestationAndProblemRepository_ThenReturnFalse() throws ObjectNotExistInDBException {
		// GIVEN
		boolean result;

		given(mockRepository.isExist(any(Firestation.class))).willReturn(true);
		given(mockRepository.deleteFirestation(any(Firestation.class))).willReturn(false);

		// WHEN
		result = service.deleteFirestation(modifiedFirestation);

		// THEN
		verify(mockRepository, times(1)).isExist(any(Firestation.class));
		verify(mockRepository, times(1)).deleteFirestation(any(Firestation.class));
		assertThat(result).isFalse();
	}

	@Test
	void testGivenUnknowFirestation_WhenDeleteFirestation_ThenThrowsObjectNotExistInDBException() throws ObjectNotExistInDBException {
		verify(mockRepository, times(0)).deleteFirestation(firestation);
		assertThrows(ObjectNotExistInDBException.class, () -> service.deleteFirestation(modifiedFirestation));
	}

}
