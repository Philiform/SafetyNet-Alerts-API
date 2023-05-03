package com.safetynetalert.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalert.api.exception.ObjectAlreadyExistInDBException;
import com.safetynetalert.api.exception.ObjectNotExistInDBException;
import com.safetynetalert.api.model.Firestation;
import com.safetynetalert.api.service.FirestationsService;

@WebMvcTest(controllers = FirestationsController.class)
public class FirestationsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirestationsService service;

	private static Firestation firestation;
	private static Firestation newFirestation;
	private static Firestation firestationBadInfo;
	private static Firestation newFirestationBadInfo;
	private static Firestation modifiedFirestation;
	private static List<Firestation> listFirestations;
	private static List<Firestation> listFirestationsAttendues;
	private static List<String> listStringAttendues;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		firestation = new Firestation();
		firestation.setAddress("112 Steppes Pl");
		firestation.setStation("4");

		newFirestation = new Firestation();
		newFirestation.setAddress("200 Java Street");
		newFirestation.setStation("4");

		firestationBadInfo = new Firestation();
		firestationBadInfo.setAddress("112 Steppes Pl");
		firestationBadInfo.setStation("");

		newFirestationBadInfo = new Firestation();
		newFirestationBadInfo.setAddress("Av");
		newFirestationBadInfo.setStation("");

		modifiedFirestation = new Firestation();
		modifiedFirestation.setAddress("112 Steppes Pl");
		modifiedFirestation.setStation("8");

		listFirestations = new ArrayList<>();

		listFirestationsAttendues = new ArrayList<>();
		listFirestationsAttendues.add(firestation);
		listFirestationsAttendues.add(newFirestation);

		listStringAttendues = new ArrayList<>();
		listStringAttendues.add("112 Steppes Pl");
		listStringAttendues.add("748 Townings Dr");
	}

	@Test
	void testGivenStationIs2_WhenGetFirestationsByStationNumber_ThenReturnOk() throws Exception {
		given(service.getFirestationsByStationNumber(anyString())).willReturn(listFirestationsAttendues);
		mockMvc.perform(get("/firestation/2")).andExpect(status().isOk());
	}

	@Test
	void testGivenStationIs5_WhenGetFirestationsByStationNumber_ThenReturnNoContent() throws Exception {
		given(service.getFirestationsByStationNumber(anyString())).willReturn(listFirestations);
		mockMvc.perform(get("/firestation/5")).andExpect(status().isNoContent());
	}

	@Test
	public void testFirestations_ThenReturnOk() throws Exception {
		given(service.getAllFirestations()).willReturn(listFirestationsAttendues);
		mockMvc.perform(get("/firestations")).andExpect(status().isOk());
	}

	@Test
	public void testFirestations_ThenReturnNoContent() throws Exception {
		given(service.getAllFirestations()).willReturn(listFirestations);
		mockMvc.perform(get("/firestations")).andExpect(status().isNoContent());
	}

	@Test
	public void testCreateFirestationWithBadInfo_ThenReturnBadRequest() throws Exception {
		mockMvc.perform(post("/firestation")
			.content(asJsonString(newFirestationBadInfo))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

	}

	@Test
	public void testCreateFirestation_ThenReturnCreated() throws Exception {
		given(service.saveFirestation(any(Firestation.class))).willReturn(Optional.of(newFirestation));

		mockMvc.perform(post("/firestation")
			.content(asJsonString(newFirestation))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated());
		}

	@Test
	public void testCreateFirestationThenRepositoryError_ThenReturnBadRequest() throws Exception {
		given(service.saveFirestation(any(Firestation.class))).willReturn(Optional.empty());

		mockMvc.perform(post("/firestation")
			.content(asJsonString(newFirestation))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateFirestationAlreadyExist_ThenReturnBadRequest() throws Exception {
		given(service.saveFirestation(any(Firestation.class))).willThrow(new ObjectAlreadyExistInDBException(""));

		mockMvc.perform(post("/firestation")
			.content(asJsonString(firestation))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdateFirestationWithBadInfo_ThenReturnBadRequest() throws Exception {
		System.out.println("firestation: " + asJsonString(firestationBadInfo));
		mockMvc.perform(put("/firestation?address=112 Steppes Pl&station=4")
			.content(asJsonString(firestationBadInfo))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

		verify(service, times(0)).updateFirestation(anyString(), anyString(), any(Firestation.class));
	}

	@Test
	public void testUpdateFirestation_ThenReturnOk() throws Exception {
		given(service.updateFirestation(anyString(), anyString(), any(Firestation.class))).willReturn(Optional.of(modifiedFirestation));

		mockMvc.perform(put("/firestation?address=112 Steppes Pl&station=4")
			.content(asJsonString(modifiedFirestation))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		}

	@Test
	public void testUpdateFirestationThenRepositoryError_ThenReturnBadRequest() throws Exception {
		given(service.updateFirestation(anyString(), anyString(), any(Firestation.class))).willReturn(Optional.empty());

		mockMvc.perform(put("/firestation?address=112 Steppes Pl&station=8")
			.content(asJsonString(newFirestation))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdateFirestationNotExist_ThenReturnBadRequest() throws Exception {
		given(service.updateFirestation(anyString(), anyString(), any(Firestation.class))).willThrow(new ObjectNotExistInDBException(""));

		mockMvc.perform(put("/firestation?address=112 Steppes Pl&station=8")
			.content(asJsonString(firestation))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testDeleteFirestationWithBadInfo_ThenReturnBadRequest() throws Exception {
		mockMvc.perform(delete("/firestation")
			.content(asJsonString(firestationBadInfo))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

	}

	@Test
	public void testDeleteFirestation_ThenReturnAccepted() throws Exception {
		given(service.deleteFirestation(any(Firestation.class))).willReturn(true);

		mockMvc.perform(delete("/firestation")
			.content(asJsonString(firestation))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isAccepted());
		}

	@Test
	public void testDeleteFirestationThenRepositoryError_ThenReturnBadRequest() throws Exception {
		given(service.deleteFirestation(any(Firestation.class))).willReturn(false);

		mockMvc.perform(delete("/firestation")
			.content(asJsonString(newFirestation))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testDeleteFirestationNotExist_ThenReturnBadRequest() throws Exception {
		given(service.deleteFirestation(any(Firestation.class))).willThrow(new ObjectNotExistInDBException(""));

		mockMvc.perform(delete("/firestation")
			.content(asJsonString(firestation))
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
