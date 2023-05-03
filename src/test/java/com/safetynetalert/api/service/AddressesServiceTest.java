package com.safetynetalert.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynetalert.api.repository.IAddressesRepository;

@ExtendWith(MockitoExtension.class)
class AddressesServiceTest {

	@InjectMocks
	private AddressesService service;

	@Mock
	private IAddressesRepository mockRepository;

	private Set<String> list = new TreeSet<>();
	private Set<String> listAttendue = new TreeSet<>();

	@Test
	void testGivenCityIsCulver_WhenGetPhonesByCity_ThenReturnTrue() {
		// GIVEN
		given(mockRepository.getPhonesByCity(anyString())).willReturn(listAttendue);

		// WHEN
		list = service.getPhonesByCity("Culver");

		// THEN
		verify(mockRepository, times(1)).getPhonesByCity(anyString());
		assertThat(list.equals(listAttendue)).isTrue();
	}

	@Test
	void testGivenCityIsEmpty_WhenGetPhonesByCity_ThenReturnEmpty() {
		// WHEN
		list = service.getPhonesByCity("");

		// THEN
		verify(mockRepository, times(0)).getPhonesByCity(anyString());
		assertThat(list).isEmpty();
	}

	@Test
	void testGivenFirestationNumberIs2_WhenGetPhonesByFirestationNumber_ThenReturnTrue() {
		// GIVEN
		given(mockRepository.getPhonesByFirestationNumber(anyString())).willReturn(listAttendue);

		// WHEN
		list = service.getPhonesByFirestationNumber("2");

		// THEN
		verify(mockRepository, times(1)).getPhonesByFirestationNumber(anyString());
		assertThat(list.equals(listAttendue)).isTrue();
	}

	@Test
	void testGivenFirestationNumberIsEmpty_WhenGetPhonesByFirestationNumber_ThenReturnEmpty() {
		// WHEN
		list = service.getPhonesByFirestationNumber("");

		// THEN
		verify(mockRepository, times(0)).getPhonesByFirestationNumber(anyString());
		assertThat(list).isEmpty();
	}

	@Test
	void testGivenCityIsCulver_WhenGetEmailsByCity_ThenReturnTrue() {
		// GIVEN
		given(mockRepository.getEmailsByCity(anyString())).willReturn(listAttendue);

		// WHEN
		list = service.getEmailsByCity("Culver");

		// THEN
		verify(mockRepository, times(1)).getEmailsByCity(anyString());
		assertThat(list.equals(listAttendue)).isTrue();
	}

	@Test
	void testGivenCityIsEmpty_WhenGetEmailsByCity_ThenReturnEmpty() {
		// WHEN
		list = service.getEmailsByCity("");

		// THEN
		verify(mockRepository, times(0)).getEmailsByCity(anyString());
		assertThat(list).isEmpty();
	}

}
