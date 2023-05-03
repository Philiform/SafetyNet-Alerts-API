package com.safetynetalert.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynetalert.api.repository.impl.AddressesRepositoryFileImpl;

@ExtendWith(MockitoExtension.class)
class AddressesRepositoryFileImplTest {

	private IAddressesRepository repository = new AddressesRepositoryFileImpl();
	private Set<String> listAddresses = new TreeSet<>();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		DataStatic.loadData("data.json");
	}

	@BeforeEach
	void init() {
		DataStatic.resetDatas();

		listAddresses.clear();
	}

	@Test
	void testGivenCityIsCulver_WhenGetPhonesByCity_ThenReturn14Phones() {
		// WHEN
		listAddresses = repository.getPhonesByCity("Culver");

		// THEN
		assertThat(listAddresses.size()).isEqualTo(14);
	}

	@Test
	void testGivenCityIsJavaCity_WhenGetPhonesByCity_ThenReturn0Phones() {
		// WHEN
		listAddresses = repository.getPhonesByCity("JavaCity");

		// THEN
		assertThat(listAddresses.size()).isEqualTo(0);
	}

	@Test
	void testGivenFirestationNumberIs2_WhenGetPhonesByFirestationNumber_ThenReturn4Phones() {
		// WHEN
		listAddresses = repository.getPhonesByFirestationNumber("2");

		// THEN
		assertThat(listAddresses.size()).isEqualTo(4);
	}

	@Test
	void testGivenFirestationNumberIs5_WhenGetPhonesByFirestationNumber_ThenReturn0Phone() {
		// WHEN
		listAddresses = repository.getPhonesByFirestationNumber("5");

		// THEN
		assertThat(listAddresses.size()).isEqualTo(0);
	}

	@Test
	void testGivenCityIsCulver_WhenGetEmailsByCity_ThenReturn15Emails() {
		// WHEN
		listAddresses = repository.getEmailsByCity("Culver");

		// THEN
		assertThat(listAddresses.size()).isEqualTo(15);
	}

	@Test
	void testGivenCityIsJavaCity_WhenGetEmailsByCity_ThenReturn0Emails() {
		// WHEN
		listAddresses = repository.getEmailsByCity("JavaCity");

		// THEN
		assertThat(listAddresses.size()).isEqualTo(0);
	}

}
