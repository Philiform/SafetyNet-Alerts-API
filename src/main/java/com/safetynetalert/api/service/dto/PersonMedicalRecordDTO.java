package com.safetynetalert.api.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonMedicalRecordDTO {

	private List<String> medications;

	private List<String> allergies;
}
