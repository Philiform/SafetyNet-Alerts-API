package com.safetynetalert.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import com.safetynetalert.api.exception.DateFutureException;
import com.safetynetalert.api.exception.ObjectAlreadyExistInDBException;
import com.safetynetalert.api.exception.ObjectNotExistInDBException;
import com.safetynetalert.api.model.Medicalrecord;
import com.safetynetalert.api.service.MedicalrecordsService;

@WebMvcTest(controllers = MedicalrecordsController.class)
public class MedicalrecordsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MedicalrecordsService service;

	private static Medicalrecord medicalrecord;
	private static Medicalrecord medicalrecordBadInfo;
	private static Medicalrecord newMedicalrecord;
	private static Medicalrecord newMedicalrecordBadInfo;
	private static Medicalrecord medicalrecordBirthdateTomorrow;
	private static Medicalrecord medicalrecordBirthdateDayIs33;
	private static List<String> listString = new ArrayList<>();

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

		medicalrecordBadInfo = new Medicalrecord();
		medicalrecordBadInfo.setFirstName("S");
		medicalrecordBadInfo.setLastName("Z");
		medicalrecordBadInfo.setBirthdate("03/04/2023");
		medicalrecordBadInfo.setMedications(listString);
		medicalrecordBadInfo.setAllergies(listString);

		newMedicalrecordBadInfo = new Medicalrecord();
		newMedicalrecordBadInfo.setFirstName("P");
		newMedicalrecordBadInfo.setLastName("F");
		newMedicalrecordBadInfo.setBirthdate("13/33/2020");
		newMedicalrecordBadInfo.setMedications(listString);
		newMedicalrecordBadInfo.setAllergies(listString);

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
	public void testGivenMedicalrecordIsFirstNameIsSophiaAndlastNameIsZemicks_ThenReturnOk() throws Exception {
		given(service.getMedicalrecordByFirstNameAndLastName(anyString(), anyString())).willReturn(Optional.of(medicalrecord));
		mockMvc.perform(get("/medicalRecord?firstName=Sophia&lastName=Zemicks")).andExpect(status().isOk());
	}

	@Test
	public void testGivenMedicalrecordIsFirstNameIsPhilAndlastNameIsFinbert_ThenReturnNoContent() throws Exception {
		given(service.getMedicalrecordByFirstNameAndLastName(anyString(), anyString())).willReturn(Optional.empty());
		mockMvc.perform(get("/medicalRecord?firstName=Sophia&lastName=Zemicks")).andExpect(status().isNoContent());
	}

	@Test
	public void testMedicalrecords_ThenReturnOk() throws Exception {
		Medicalrecord medicalrecord = new Medicalrecord();
		List<Medicalrecord> medicalrecords = new ArrayList<>();
		medicalrecords.add(medicalrecord);

		given(service.getAllMedicalrecords()).willReturn(medicalrecords);

		mockMvc.perform(get("/medicalRecords")).andExpect(status().isOk());
	}

	@Test
	public void testMedicalrecords_ThenReturnNoContent() throws Exception {
		given(service.getAllMedicalrecords()).willReturn(new ArrayList<Medicalrecord>());
		mockMvc.perform(get("/medicalRecords")).andExpect(status().isNoContent());
	}

	@Test
	public void testGivenNewMedicalrecordWithTomorrowBirthDate_WhenSaveMedicalrecord_ThenThrowsDateFutureException() throws Exception {
		given(service.isBirthdateValid(anyString())).willThrow(new DateFutureException(""));

		mockMvc.perform(post("/medicalRecord")
			.content(asJsonString(medicalrecordBirthdateTomorrow))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testGivenNewMedicalrecordWithBirthDateDayIs33_WhenSaveMedicalrecord_ThenThrowsDateTimeParseException() throws Exception {
		given(service.isBirthdateValid(anyString())).willThrow(new DateTimeParseException("", "", 0));

		mockMvc.perform(post("/medicalRecord")
			.content(asJsonString(medicalrecordBirthdateDayIs33))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateMedicalrecordWithBadInfo_ThenReturnBadRequest() throws Exception {
		mockMvc.perform(post("/medicalRecord")
			.content(asJsonString(newMedicalrecordBadInfo))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

	}

	@Test
	public void testCreateMedicalrecord_ThenReturnCreated() throws Exception {
		given(service.saveMedicalrecord(any(Medicalrecord.class))).willReturn(Optional.of(newMedicalrecord));

		mockMvc.perform(post("/medicalRecord")
			.content(asJsonString(newMedicalrecord))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated());
		}

	@Test
	public void testCreateMedicalrecordThenRepositoryError_ThenReturnBadRequest() throws Exception {
		given(service.saveMedicalrecord(any(Medicalrecord.class))).willReturn(Optional.empty());

		mockMvc.perform(post("/medicalRecord")
			.content(asJsonString(newMedicalrecord))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateMedicalrecordAlreadyExist_ThenReturnBadRequest() throws Exception {
		given(service.saveMedicalrecord(any(Medicalrecord.class))).willThrow(new ObjectAlreadyExistInDBException(""));

		mockMvc.perform(post("/medicalRecord")
			.content(asJsonString(medicalrecord))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testGivenModifiedMedicalrecordWithTomorrowBirthDate_WhenSaveMedicalrecord_ThenThrowsDateFutureException() throws Exception {
		given(service.isBirthdateValid(anyString())).willThrow(new DateFutureException(""));

		mockMvc.perform(put("/medicalRecord")
			.content(asJsonString(medicalrecordBirthdateTomorrow))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testGivenModifiedMedicalrecordWithBirthDateDayIs33_WhenSaveMedicalrecord_ThenThrowsDateTimeParseException() throws Exception {
		given(service.isBirthdateValid(anyString())).willThrow(new DateTimeParseException("", "", 0));

		mockMvc.perform(put("/medicalRecord")
			.content(asJsonString(medicalrecordBirthdateDayIs33))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdateMedicalrecordWithBadInfo_ThenReturnBadRequest() throws Exception {
		mockMvc.perform(put("/medicalRecord")
			.content(asJsonString(medicalrecordBadInfo))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdateMedicalrecord_ThenReturnOk() throws Exception {
		given(service.updateMedicalrecord(any(Medicalrecord.class))).willReturn(Optional.of(medicalrecord));

		mockMvc.perform(put("/medicalRecord")
			.content(asJsonString(medicalrecord))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		}

	@Test
	public void testUpdateMedicalrecordThenRepositoryError_ThenReturnBadRequest() throws Exception {
		given(service.updateMedicalrecord(any(Medicalrecord.class))).willReturn(Optional.empty());

		mockMvc.perform(put("/medicalRecord")
			.content(asJsonString(newMedicalrecord))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdateMedicalrecordNotExist_ThenReturnBadRequest() throws Exception {
		given(service.updateMedicalrecord(any(Medicalrecord.class))).willThrow(new ObjectNotExistInDBException(""));

		mockMvc.perform(put("/medicalRecord")
			.content(asJsonString(medicalrecord))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testDeleteMedicalrecordWithBadInfo_ThenReturnBadRequest() throws Exception {
		mockMvc.perform(delete("/medicalRecord")
			.content(asJsonString(medicalrecordBadInfo))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());

	}

	@Test
	public void testDeleteMedicalrecord_ThenReturnAccepted() throws Exception {
		given(service.deleteMedicalrecord(any(Medicalrecord.class))).willReturn(true);

		mockMvc.perform(delete("/medicalRecord")
			.content(asJsonString(medicalrecord))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isAccepted());
		}

	@Test
	public void testDeleteMedicalrecordThenRepositoryError_ThenReturnBadRequest() throws Exception {
		given(service.deleteMedicalrecord(any(Medicalrecord.class))).willReturn(false);

		mockMvc.perform(delete("/medicalRecord")
			.content(asJsonString(newMedicalrecord))
		    .contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testDeleteMedicalrecordNotExist_ThenReturnBadRequest() throws Exception {
		given(service.deleteMedicalrecord(any(Medicalrecord.class))).willThrow(new ObjectNotExistInDBException(""));

		mockMvc.perform(delete("/medicalRecord")
			.content(asJsonString(medicalrecord))
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
