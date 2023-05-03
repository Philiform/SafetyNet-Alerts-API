package com.safetynetalert.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.safetynetalert.api.model.Medicalrecord;
import com.safetynetalert.api.repository.impl.MedicalrecordsRepositoryFileImpl;

class MedicalrecordsRepositoryFileImplTest {

	private IMedicalrecordsRepository repository = new MedicalrecordsRepositoryFileImpl();
	private Optional<String> infoString;
	private Optional<Medicalrecord> medicalrecord;
	private Medicalrecord medicalrecordAttendu;
	private List<String> listString = new ArrayList<>();
	private List<Medicalrecord> listMedicalrecord = new ArrayList<>();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		DataStatic.loadData("data.json");
	}

	@BeforeEach
	void init() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		DataStatic.resetDatas();
	}

	@Test
	void testGivenFirstNameIsEricAndlastNameIsCadigan_WhenGetBirthdateByFirstNameAndLastName_ThenReturn08_06_1945() {
		// GIVEN
		infoString = Optional.empty();

		// WHEN
		infoString = repository.getBirthdateByFirstNameAndLastName("Eric", "Cadigan");

		// THEN
		assertThat(infoString).isEqualTo(Optional.of("08/06/1945"));
	}

	@Test
	void testGivenFirstNameIsAlbertAndLastNameIsEinstein_WhenGetBirthdateByFirstNameAndLastName_ThenReturnEmpty() {
		// GIVEN
		infoString = Optional.empty();

		// WHEN
		infoString = repository.getBirthdateByFirstNameAndLastName("Albert", "Einstein");

		// THEN
		assertThat(infoString).isEqualTo(Optional.empty());
	}

	@Test
	void testGivenFirstNameIsSophiaAndlastNameIsZemicks_WhenGetMedicalrecordByFirstNameAndLastName_ThenReturnMedicalrecord() {
		// GIVEN
		medicalrecord = Optional.empty();
		List<Medicalrecord> list = DataStatic.getDatas().getMedicalrecords().stream().toList();

		// WHEN
		medicalrecord = repository.getMedicalrecordByFirstNameAndLastName("Sophia", "Zemicks");

		// THEN
		assertThat(medicalrecord).isEqualTo(Optional.of(list.get(11)));
	}

	@Test
	void testGivenFirstNameIsAlbertAndLastNameIsEinstein_WhenGetMedicalrecordByFirstNameAndLastName_ThenReturnMedicalrecordEmpty() {
		// GIVEN
		medicalrecord = Optional.empty();

		// WHEN
		medicalrecord = repository.getMedicalrecordByFirstNameAndLastName("Albert", "Einstein");

		// THEN
		assertThat(medicalrecord).isEqualTo(Optional.empty());
	}

	@Test
	void testGivenNothing_WhenGetAllMedicalrecords_ThenReturn23Medicalrecords() {
		// GIVEN
		listMedicalrecord.clear();

		// WHEN
		listMedicalrecord = repository.getAllMedicalrecords();

		// THEN
		assertThat(listMedicalrecord.size()).isEqualTo(23);
		assertThat(listMedicalrecord.get(8)).isEqualTo(DataStatic.getDatas().getMedicalrecords().get(8));
	}

	@Test
	void testGivenNewMedicalrecord_WhenSaveMedicalrecord_ThenReturn1Medicalrecord() {
		// GIVEN
		Medicalrecord medicalrecord1 = new Medicalrecord();
		medicalrecordAttendu = new Medicalrecord();

		medicalrecordAttendu.setFirstName("Albert");
		medicalrecordAttendu.setLastName("Einstein");
		medicalrecordAttendu.setBirthdate("01/01/2023");
		medicalrecordAttendu.setMedications(listString);
		medicalrecordAttendu.setAllergies(listString);

		// WHEN
		medicalrecord1 = repository.saveMedicalrecord(medicalrecordAttendu).get();

		// THEN
		assertThat(medicalrecord1).isEqualTo(medicalrecordAttendu);
		assertThat(DataStatic.getDatas().getMedicalrecords().size()).isEqualTo(24);
	}

	@Test
	void testGivenMedicalrecordModified_WhenUpdateMedicalrecord_ThenReturn1MedicalrecordModified() {
		// GIVEN
		Medicalrecord medicalrecord1 = new Medicalrecord();
		Medicalrecord medicalrecordOld;

		medicalrecordOld = new Medicalrecord(DataStatic.getDatas().getMedicalrecords().get(21));

		medicalrecordAttendu = new Medicalrecord(DataStatic.getDatas().getMedicalrecords().get(21));
		medicalrecordAttendu.setBirthdate("08/16/1995");

		// WHEN
		medicalrecord1 = repository.updateMedicalrecord(medicalrecordAttendu).get();

		// THEN
		assertThat(medicalrecord1).isEqualTo(medicalrecordAttendu);
		assertThat(DataStatic.getDatas().getMedicalrecords().size()).isEqualTo(23);
		assertThat(DataStatic.getDatas().getMedicalrecords().contains(medicalrecord1)).isTrue();
		assertThat(DataStatic.getDatas().getMedicalrecords().contains(medicalrecordOld)).isFalse();
	}

	@Test
	void testGivenMedicalrecord_WhenDeleteMedicalrecord_ThenReturn22Medicalrecords() {
		// GIVEN
		medicalrecordAttendu = new Medicalrecord(DataStatic.getDatas().getMedicalrecords().get(21));

		// WHEN
		repository.deleteMedicalrecord(medicalrecordAttendu);

		// THEN
		assertThat(DataStatic.getDatas().getMedicalrecords().size()).isEqualTo(22);
		assertThat(DataStatic.getDatas().getMedicalrecords().contains(medicalrecordAttendu)).isFalse();
	}

	@Test
	void testGivenMedicalrecordUnodified_WhenIsExist_ThenReturnTrue() {
		// GIVEN
		boolean result = true;

		medicalrecordAttendu = new Medicalrecord(DataStatic.getDatas().getMedicalrecords().get(21));

		// WHEN
		result = repository.isExist(medicalrecordAttendu);

		// THEN
		assertThat(result).isTrue();
	}

	@Test
	void testGivenUnknowMedicalrecord_WhenIsExist_ThenReturnFalse() {
		// GIVEN
		boolean result = false;

		medicalrecordAttendu = new Medicalrecord(DataStatic.getDatas().getMedicalrecords().get(21));
		medicalrecordAttendu.setLastName("Ferguson2");

		// WHEN
		result = repository.isExist(medicalrecordAttendu);

		// THEN
		assertThat(result).isFalse();
	}

}
