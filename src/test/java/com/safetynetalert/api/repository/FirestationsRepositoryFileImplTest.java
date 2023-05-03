package com.safetynetalert.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.safetynetalert.api.model.Firestation;
import com.safetynetalert.api.repository.impl.FirestationsRepositoryFileImpl;

class FirestationsRepositoryFileImplTest {

	private IFirestationsRepository repository = new FirestationsRepositoryFileImpl();
	private Firestation firestationAttendue;
	private List<Firestation> listFirestations = new ArrayList<>();
	private List<Firestation> listFirestationsAttendues = new ArrayList<>();
	private List<String> listString = new ArrayList<>();
	private List<String> listStringAttendues = new ArrayList<>();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		DataStatic.loadData("data.json");
	}

	@BeforeEach
	void init() {
		DataStatic.resetDatas();
	}

	@Test
	void testGivenStationIs2_WhenGetAddressesByStationNumber_ThenReturn3Addresses() {
		// GIVEN
		listString.clear();
		listStringAttendues.clear();

		listStringAttendues.add("29 15th St");
		listStringAttendues.add("892 Downing Ct");
		listStringAttendues.add("951 LoneTree Rd");

		// WHEN
		listString = repository.getAddressesByStationNumber("2");

		// THEN
		assertThat(listString).isEqualTo(listStringAttendues);
		assertThat(listString.get(1)).isEqualTo("892 Downing Ct");
	}

	@Test
	void testGivenStationIs5_WhenGetAddressesByStationNumber_ThenReturn0Address() {
		// GIVEN
		listString.clear();

		// WHEN
		listString = repository.getAddressesByStationNumber("5");

		// THEN
		assertThat(listString.size()).isEqualTo(0);
	}

	@Test
	void testGivenStationIs2_WhenGetFirestationsByStationNumber_ThenReturn3Addresses() {
		// GIVEN
		listFirestations.clear();
		listFirestationsAttendues.clear();

		listFirestationsAttendues.add(DataStatic.getDatas().getFirestations().get(1));
		listFirestationsAttendues.add(DataStatic.getDatas().getFirestations().get(7));
		listFirestationsAttendues.add(DataStatic.getDatas().getFirestations().get(11));

		// WHEN
		listFirestations = repository.getFirestationsByStationNumber("2");

		// THEN
		assertThat(listFirestations).isEqualTo(listFirestationsAttendues);
		assertThat(listFirestations.get(1).getAddress()).isEqualTo("892 Downing Ct");
	}

	@Test
	void testGivenStationIs5_WhenGetFirestationsByStationNumber_ThenReturn0Address() {
		// GIVEN
		listFirestations.clear();

		// WHEN
		listFirestations = repository.getFirestationsByStationNumber("5");
		// THEN
		assertThat(listFirestations.size()).isEqualTo(0);
	}

	@Test
	void testGivenAddresStationIs112SteppesPl_WhenGetStationsNumbersByAdress_ThenReturn2Addresses() {
		// GIVEN
		listString.clear();
		listStringAttendues.clear();

		listStringAttendues.add("3");
		listStringAttendues.add("4");

		// WHEN
		listString = repository.getStationsNumbersByAdress("112 Steppes Pl");

		// THEN
		assertThat(listString).isEqualTo(listStringAttendues);
		assertThat(listString.size()).isEqualTo(2);
	}

	@Test
	void testGivenAddresStationIs200TowningsDr_WhenGetStationsNumbersByAdress_ThenReturn0StationNumber() {
		// GIVEN
		listString.clear();
		listStringAttendues.clear();

		// WHEN
		listString = repository.getStationsNumbersByAdress("200 Townings Dr");

		// THEN
		assertThat(listString).isEqualTo(listStringAttendues);
	}

	@Test
	void testGivenAddresStationIs112SteppesPl_WhenGetFirestationsByAdress_ThenReturn2Addresses() {
		// GIVEN
		listFirestations.clear();
		listFirestationsAttendues.clear();

		listFirestationsAttendues.add(DataStatic.getDatas().getFirestations().get(5));
		listFirestationsAttendues.add(DataStatic.getDatas().getFirestations().get(9));

		// WHEN
		listFirestations = repository.getFirestationsByAdress("112 Steppes Pl");

		// THEN
		assertThat(listFirestations).isEqualTo(listFirestationsAttendues);
		assertThat(listFirestations.size()).isEqualTo(2);
	}

	@Test
	void testGivenAddresStationIs200TowningsDr_WhenGetStationNumberByAdress_ThenReturn0StationNumber() {
		// GIVEN
		listFirestations.clear();
		listFirestationsAttendues.clear();

		// WHEN
		listFirestations = repository.getFirestationsByAdress("200 Townings Dr");

		// THEN
		assertThat(listFirestations).isEqualTo(listFirestationsAttendues);
	}

	@Test
	void testGivenNothing_WhenGetAllFirestations_ThenReturn12Firestations() {
		// GIVEN
		listFirestations.clear();

		// WHEN
		listFirestations = repository.getAllFirestations();

		// THEN
		assertThat(listFirestations.size()).isEqualTo(12);
		assertThat(listFirestations.get(8)).isEqualTo(DataStatic.getDatas().getFirestations().get(8));
	}

	@Test
	void testGivenNewFirestation_WhenSaveFirestation_ThenReturn1Firestation() {
		// GIVEN
		Firestation firestation1 = new Firestation();
		firestationAttendue = new Firestation();

		firestationAttendue.setAddress("200 Townings Dr");
		firestationAttendue.setStation("5");

		// WHEN
		firestation1 = repository.saveFirestation(firestationAttendue).get();

		// THEN
		assertThat(firestation1).isEqualTo(firestationAttendue);
		assertThat(DataStatic.getDatas().getFirestations().size()).isEqualTo(13);
	}

	@Test
	void testGivenAddresStationIs112SteppesPlAndNumber4AndNewFirestation_WhenUpdataFirestation_ThenReturn1FirestationModified() {
		// GIVEN
		Firestation firestation1 = new Firestation();
		Firestation firestationOld;

		firestationOld = new Firestation(DataStatic.getDatas().getFirestations().get(3));

		firestationAttendue = new Firestation(DataStatic.getDatas().getFirestations().get(3));
		firestationAttendue.setStation("5");

		// WHEN
		firestation1 = repository.updateFirestation(firestationOld.getAddress(), firestationOld.getStation(), firestationAttendue).get();

		// THEN
		assertThat(firestation1).isEqualTo(firestationAttendue);
		assertThat(DataStatic.getDatas().getFirestations().size()).isEqualTo(12);
		assertThat(DataStatic.getDatas().getFirestations().contains(firestation1)).isTrue();
		assertThat(DataStatic.getDatas().getFirestations().contains(firestationOld)).isFalse();
	}

	@Test
	void testGivenFirestation_WhenDeleteFirestation_ThenReturn11Firestations() {
		// GIVEN
		firestationAttendue = new Firestation();

		firestationAttendue.setAddress("748 Townings Dr");
		firestationAttendue.setStation("3");

		// WHEN
		repository.deleteFirestation(firestationAttendue);

		// THEN
		assertThat(repository.getAllFirestations().size()).isEqualTo(11);
		assertThat(DataStatic.getDatas().getFirestations().contains(firestationAttendue)).isFalse();
	}

	@Test
	void testGivenFirestation_WhenIsExist_ThenReturnTrue() {
		// GIVEN
		boolean result = false;

		firestationAttendue = new Firestation(DataStatic.getDatas().getFirestations().get(3));

		// WHEN
		result = repository.isExist(firestationAttendue);

		// THEN
		assertThat(result).isTrue();
	}

	@Test
	void testGivenFirestation_WhenIsExist_ThenReturnFalse() {
		// GIVEN
		boolean result = true;

		firestationAttendue = new Firestation(DataStatic.getDatas().getFirestations().get(3));
		firestationAttendue.setStation("8");

		// WHEN
		result = repository.isExist(firestationAttendue);

		// THEN
		assertThat(result).isFalse();
	}

}
