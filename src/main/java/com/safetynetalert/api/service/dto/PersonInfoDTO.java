package com.safetynetalert.api.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonInfoDTO {

	@Schema(example = "Boyd")
	private String lastName;

	@Schema(example = "1509 Culver St")
	private String address;

	@Schema(example = "39")
	private String age;

	@Schema(example = "jaboyd@email.com")
	private String email;

	private PersonMedicalRecordDTO medicalRecord;
}
