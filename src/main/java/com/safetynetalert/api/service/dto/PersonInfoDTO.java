package com.safetynetalert.api.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonInfoDTO {

	private String lastName;

	private String address;

	private String age;

	private String email;

	private PersonMedicalRecordDTO medicalRecord;
}
