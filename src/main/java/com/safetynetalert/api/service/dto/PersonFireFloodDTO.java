package com.safetynetalert.api.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonFireFloodDTO {

	private String lastName;

	private String phone;

	private String age;

	private PersonMedicalRecordDTO medicalRecord;
}
