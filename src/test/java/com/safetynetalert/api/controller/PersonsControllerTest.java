package com.safetynetalert.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalert.api.exception.ObjectAlreadyExistInDBException;
import com.safetynetalert.api.exception.ObjectNotExistInDBException;
import com.safetynetalert.api.model.Person;
import com.safetynetalert.api.service.PersonsService;
import com.safetynetalert.api.service.dto.ChildAlertDTO;
import com.safetynetalert.api.service.dto.FireDTO;
import com.safetynetalert.api.service.dto.FirestationDTO;
import com.safetynetalert.api.service.dto.FloodDTO;
import com.safetynetalert.api.service.dto.PersonFireFloodDTO;
import com.safetynetalert.api.service.dto.PersonFirestationDTO;
import com.safetynetalert.api.service.dto.PersonInfoDTO;

@WebMvcTest(controllers = PersonsController.class)
public class PersonsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PersonsService service;

	private String goodAddress = "1509 Culver St";
	private String badAddress = "1509 Culver St2";
	private PersonFireFloodDTO personFireFloodDTO = new PersonFireFloodDTO();

	private FirestationDTO firestationDTO;
	private FireDTO fireDTO;

	private static Person person;
	private static Person personBadInfo;
	private static Person newPerson;
	private static Person newPersonBadInfo;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		person = new Person();
		person.setFirstName("Sophia");
		person.setLastName("Zemicks");
		person.setAddress("892 Downing Ct");
		person.setCity("Culver");
		person.setZip("97451");
		person.setPhone("841-874-7878");
		person.setEmail("soph@email.com");

		newPerson = new Person();
		newPerson.setFirstName("Phil");
		newPerson.setLastName("Finbert");
		newPerson.setAddress("4 rue du Java");
		newPerson.setCity("Poo");
		newPerson.setZip("42");
		newPerson.setPhone("06 07 08 09 01");
		newPerson.setEmail("phil@java.com");

		newPersonBadInfo = new Person();
		newPersonBadInfo.setFirstName("P");
		newPersonBadInfo.setLastName("F");
		newPersonBadInfo.setAddress("4");
		newPersonBadInfo.setCity("P");
		newPersonBadInfo.setZip("42");
		newPersonBadInfo.setPhone("06 07 08 09 01");
		newPersonBadInfo.setEmail("philjava.com");

		personBadInfo = new Person();
		personBadInfo.setFirstName("Sophia");
		personBadInfo.setLastName("Zemicks");
		personBadInfo.setAddress("4");
		personBadInfo.setCity("P");
		personBadInfo.setZip("42");
		personBadInfo.setPhone("06 07 08 09 01");
		personBadInfo.setEmail("philjava.com");
	}

	@Test
	public void testGivenPasDePages_ThenReturnNotFound() throws Exception {
		mockMvc.perform(get("/pasDePages")).andExpect(status().isNotFound());
	}

	@Test
	public void testGivenFirestationStationNumberIs1_ThenReturnOk() throws Exception {
		// GIVEN
		PersonFirestationDTO personFirestationDTO = new PersonFirestationDTO();
		List<PersonFirestationDTO> listPersonFirestationDTO = new ArrayList<>();
		firestationDTO = new FirestationDTO();
		listPersonFirestationDTO.add(personFirestationDTO);
		firestationDTO.setPersons(listPersonFirestationDTO);

		given(service.getListPersonsByStation(anyString())).willReturn(firestationDTO);

		// THEN
		mockMvc.perform(get("/firestation?stationNumber=1")).andExpect(status().isOk());
	}

	@Test
	public void testGivenFirestationStationNumberIs8_ThenReturnNoContent() throws Exception {
		// GIVEN
		List<PersonFirestationDTO> listPersonFirestationDTO = new ArrayList<>();
		firestationDTO = new FirestationDTO();
		firestationDTO.setPersons(listPersonFirestationDTO);

		given(service.getListPersonsByStation(anyString())).willReturn(firestationDTO);

		// THEN
		mockMvc.perform(get("/firestation?stationNumber=8")).andExpect(status().isNoContent());
	}

	@Test
	public void testGivenChildAlertAddressIs1509CulverSt_ThenReturnOk() throws Exception {
		// GIVEN
		ChildAlertDTO childAlertDTO = new ChildAlertDTO();
		List<ChildAlertDTO> listChildAlertDTO = new ArrayList<>();
		listChildAlertDTO.add(childAlertDTO);

		given(service.getListChildByAdress(anyString())).willReturn(listChildAlertDTO);

		// THEN
		mockMvc.perform(get("/childAlert?address=" + goodAddress)).andExpect(status().isOk());
	}

	@Test
	public void testGivenChildAlertAddressIs1509CulverSt2_ThenReturnNoContent() throws Exception {
		// GIVEN
		List<ChildAlertDTO> listChildAlertDTO = new ArrayList<>();
		given(service.getListChildByAdress(anyString())).willReturn(listChildAlertDTO);

		// THEN
		mockMvc.perform(get("/childAlert?address=" + badAddress)).andExpect(status().isNoContent());
	}

	@Test
	public void testGivenFireAddressIs1509CulverSt_ThenReturnOk() throws Exception {
		// GIVEN
		List<PersonFireFloodDTO> listPersonFireFloodDTO = new ArrayList<>();
		fireDTO = new FireDTO();
		listPersonFireFloodDTO.add(personFireFloodDTO);
		fireDTO.setPersons(listPersonFireFloodDTO);

		given(service.getListPersonsByAdressFire(anyString())).willReturn(fireDTO);

		// THEN
		mockMvc.perform(get("/fire?address=" + goodAddress)).andExpect(status().isOk());
	}

	@Test
	public void testGivenFireAddressIs1509CulverSt_ThenReturnNoContent() throws Exception {
		// GIVEN
		List<PersonFireFloodDTO> listPersonFireFloodDTO = new ArrayList<>();
		fireDTO = new FireDTO();
		fireDTO.setPersons(listPersonFireFloodDTO);

		given(service.getListPersonsByAdressFire(anyString())).willReturn(fireDTO);

		// THEN
		mockMvc.perform(get("/fire?address=" + badAddress)).andExpect(status().isNoContent());
	}

	@Test
	public void testGivenFloodStationsAre1And2_ThenReturnOk() throws Exception {
		// GIVEN
		FloodDTO floodDTO = new FloodDTO();
		List<FloodDTO> listFloodDTO = new ArrayList<>();
		listFloodDTO.add(floodDTO);

		given(service.getListPersonsByStationsNumbersFlood(ArgumentMatchers.<String>anyList())).willReturn(listFloodDTO);

		// THEN
		mockMvc.perform(get("/flood/stations?stations=1,2")).andExpect(status().isOk());
	}

	@Test
	public void testGivenFloodStationsIs8_ThenReturnNoContent() throws Exception {
		// GIVEN
		List<FloodDTO> listFloodDTO = new ArrayList<>();

		given(service.getListPersonsByStationsNumbersFlood(ArgumentMatchers.<String>anyList())).willReturn(listFloodDTO);

		// THEN
		mockMvc.perform(get("/flood/stations?stations=8")).andExpect(status().isNoContent());
	}

	@Test
	public void testGivenPersonInfoIsFirstNameIsSophiaAndlastNameIsZemicks_ThenReturnOk() throws Exception {
		// GIVEN
		PersonInfoDTO personInfoDTO = new PersonInfoDTO();
		List<PersonInfoDTO> listPersonInfoDTO = new ArrayList<>();
		listPersonInfoDTO.add(personInfoDTO);

		given(service.getPersonInfoByFirstNameAndLastName(anyString(), anyString())).willReturn(listPersonInfoDTO);

		// THEN
		mockMvc.perform(get("/personInfo?firstName=Sophia&lastName=Zemicks")).andExpect(status().isOk());
	}

	@Test
	public void testGivenPersonInfoIsFirstNameIsSophiaAndlastNameIsZemicksz_ThenReturnNoContent() throws Exception {
		// GIVEN
		List<PersonInfoDTO> listPersonInfoDTO = new ArrayList<>();

		given(service.getPersonInfoByFirstNameAndLastName(anyString(), anyString())).willReturn(listPersonInfoDTO);

		// THEN
		mockMvc.perform(get("/personInfo?firstName=Sophia&lastName=Zemicksz")).andExpect(status().isNoContent());
	}

	@Test
	public void testGivenPersonIsFirstNameIsSophiaAndlastNameIsZemicks_ThenReturnOk() throws Exception {
		given(service.getPerson(anyString(), anyString())).willReturn(Optional.of(person));
		mockMvc.perform(get("/person?firstName=Sophia&lastName=Zemicks")).andExpect(status().isOk());
	}

	@Test
	public void testGivenPersonIsFirstNameIsPhilAndlastNameIsFinbert_ThenReturnNoContent() throws Exception {
		System.out.println("++++");
		given(service.getPerson(anyString(), anyString())).willReturn(Optional.empty());
		mockMvc.perform(get("/person?firstName=Phil&lastName=Finbert")).andExpect(status().isNoContent());
	}

	@Test
	public void testPersons_ThenReturnOk() throws Exception {
		Person person = new Person();
		List<Person> persons = new ArrayList<>();
		persons.add(person);

		given(service.getAllPersons()).willReturn(persons);

		mockMvc.perform(get("/persons")).andExpect(status().isOk());
	}

	@Test
	public void testPersons_ThenReturnNoContent() throws Exception {
		given(service.getAllPersons()).willReturn(new ArrayList<Person>());
		mockMvc.perform(get("/persons")).andExpect(status().isNoContent());
	}

	@Test
	public void testCreatePersonWithBadInfo_ThenReturnBadRequest() throws Exception {
		mockMvc.perform(post("/person")
			.content(asJsonString(newPersonBadInfo))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

	}

	@Test
	public void testCreatePerson_ThenReturnCreated() throws Exception {
		given(service.savePerson(any(Person.class))).willReturn(Optional.of(newPerson));

		mockMvc.perform(post("/person")
			.content(asJsonString(newPerson))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated());
		}

	@Test
	public void testCreatePersonThenRepositoryError_ThenReturnBadRequest() throws Exception {
		given(service.savePerson(any(Person.class))).willReturn(Optional.empty());

		mockMvc.perform(post("/person")
			.content(asJsonString(newPerson))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreatePersonAlreadyExist_ThenReturnBadRequest() throws Exception {
		given(service.savePerson(any(Person.class))).willThrow(new ObjectAlreadyExistInDBException(""));

		mockMvc.perform(post("/person")
			.content(asJsonString(person))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdatePersonWithBadInfo_ThenReturnBadRequest() throws Exception {
		mockMvc.perform(put("/person")
			.content(asJsonString(personBadInfo))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

	}

	@Test
	public void testUpdatePerson_ThenReturnOk() throws Exception {
		given(service.updatePerson(any(Person.class))).willReturn(Optional.of(person));

		mockMvc.perform(put("/person")
			.content(asJsonString(person))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		}

	@Test
	public void testUpdatePersonThenRepositoryError_ThenReturnBadRequest() throws Exception {
		given(service.updatePerson(any(Person.class))).willReturn(Optional.empty());

		mockMvc.perform(put("/person")
			.content(asJsonString(newPerson))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdatePersonNotExist_ThenReturnBadRequest() throws Exception {
		given(service.updatePerson(any(Person.class))).willThrow(new ObjectNotExistInDBException(""));

		mockMvc.perform(put("/person")
			.content(asJsonString(person))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testDeletePersonWithBadInfo_ThenReturnBadRequest() throws Exception {
		mockMvc.perform(delete("/person")
			.content(asJsonString(personBadInfo))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

	}

	@Test
	public void testDeletePerson_ThenReturnAccepted() throws Exception {
		given(service.deletePerson(any(Person.class))).willReturn(true);

		mockMvc.perform(delete("/person")
			.content(asJsonString(person))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isAccepted());
		}

	@Test
	public void testDeletePersonThenRepositoryError_ThenReturnBadRequest() throws Exception {
		given(service.deletePerson(any(Person.class))).willReturn(false);

		mockMvc.perform(delete("/person")
			.content(asJsonString(newPerson))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testDeletePersonNotExist_ThenReturnBadRequest() throws Exception {
		given(service.deletePerson(any(Person.class))).willThrow(new ObjectNotExistInDBException(""));

		mockMvc.perform(delete("/person")
			.content(asJsonString(person))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	public static String asJsonString(final Object obj) {
		try {
		    return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
	}

}
