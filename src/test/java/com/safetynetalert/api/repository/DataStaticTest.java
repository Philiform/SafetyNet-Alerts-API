package com.safetynetalert.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.safetynetalert.api.model.Datas;

class DataStaticTest {

	private Datas datas;
	private String info;

	@Test
	void testGivenReadFileJson_WhenLoadData_ThenReturnDatasNotNull() {
		// GIVEN
		DataStatic.loadData("data.json");
		DataStatic.resetDatas();

		// WHEN
		datas = DataStatic.getDatas();

		// THEN
		assertThat(datas).isNotNull();
	}

	@Test
	void testGivenFilePathIsEmpty_WhenLoadData_ThenReturnDatasNull() {
		// GIVEN
		DataStatic.loadData("");

		// WHEN
		datas = DataStatic.getDatas();

		// THEN
		assertThat(datas).isNull();
	}

	@Test
	void testGivenReadUnknowFile_WhenLoadData_ThenReturnDatasNull() {
		// GIVEN
		DataStatic.loadData("test.json");

		// WHEN
		datas = DataStatic.getDatas();

		// THEN
		assertThat(datas).isNull();
	}

	@Test
	void testGivenReadFileEmpty_WhenLoadData_ThenReturnDatasNotNull() {
		// GIVEN
		DataStatic.loadData("dataEmpty.json");

		// WHEN
		datas = DataStatic.getDatas();

		// THEN
		assertThat(datas).isNotNull();
	}

	@Test
	void testGivenReadFileJson_WhenReadFirstNamePerson_ThenReturnJohn() {
		// GIVEN
		DataStatic.loadData("data.json");
		DataStatic.resetDatas();

		// WHEN
		info = DataStatic.getDatas().getPersons().get(0).getFirstName();

		// THEN
		assertThat(info).isEqualTo("John");
	}

	@Test
	void testGivenReadFileJson_WhenReadAddressOfFirestation3_ThenReturn644GershwinCir() {
		// GIVEN
		DataStatic.loadData("data.json");
		DataStatic.resetDatas();

		// WHEN
		info = DataStatic.getDatas().getFirestations().get(3).getAddress();

		// THEN
		assertThat(info).isEqualTo("644 Gershwin Cir");
	}

	@Test
	void testGivenReadFileJson_WhenReadAllergieofPerson11_ThenReturnAznol() {
		// GIVEN
		DataStatic.loadData("data.json");
		DataStatic.resetDatas();

		// WHEN
		info = DataStatic.getDatas().getMedicalrecords().get(11).getAllergies().get(2);

		// THEN
		assertThat(info).isEqualTo("aznol");
	}
}
