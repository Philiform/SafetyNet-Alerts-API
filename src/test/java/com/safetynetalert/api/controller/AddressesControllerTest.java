package com.safetynetalert.api.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynetalert.api.service.AddressesService;

@WebMvcTest(controllers = AddressesController.class)
public class AddressesControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AddressesService service;

	private static Set<String> listPhones = new TreeSet<>();
	private Set<String> listPhonesVides = new TreeSet<>();
	private static Set<String> listEmails = new TreeSet<>();
	private Set<String> listEmailsVides = new TreeSet<>();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		listPhones.add("01 02 03 04 05");
		listPhones.add("06 07 08 09 09");

		listEmails.add("java@email.com");
		listEmails.add("develop@email.com");
	}

	@Test
	void testGivenFirestationNumberIs2_WhenGetPhonesByFirestationNumber_ThenReturnOk() throws Exception {
		given(service.getPhonesByFirestationNumber(anyString())).willReturn(listPhones);
		mockMvc.perform(get("/phoneAlert?firestation=2")).andExpect(status().isOk());
	}

	@Test
	void testGivenFirestationNumberIs5_WhenGetPhonesByFirestationNumber_ThenReturnNoContent() throws Exception {
		given(service.getPhonesByFirestationNumber(anyString())).willReturn(listPhonesVides);
		mockMvc.perform(get("/phoneAlert?firestation=5")).andExpect(status().isNoContent());
	}

	@Test
	void testGivenCityIsCulver_WhenGetEmailsByCity_ThenReturnOk() throws Exception {
		given(service.getEmailsByCity(anyString())).willReturn(listEmails);
		mockMvc.perform(get("/communityEmail?city=Culver")).andExpect(status().isOk());
	}

	@Test
	void testGivenCityIsJavaCity_WhenGetEmailsByCity_ThenReturnNoContent() throws Exception {
		given(service.getEmailsByCity(anyString())).willReturn(listEmailsVides);
		mockMvc.perform(get("/communityEmail?city=JavaCity")).andExpect(status().isNoContent());
	}

}
