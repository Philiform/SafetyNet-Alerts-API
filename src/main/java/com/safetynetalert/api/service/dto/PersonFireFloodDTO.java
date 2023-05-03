package com.safetynetalert.api.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonFireFloodDTO {

	@Schema(example = "Boyd")
	private String lastName;

	@Schema(example = "841-874-6512")
	private String phone;

	@Schema(example = "39")
	private String age;

	private PersonMedicalRecordDTO medicalRecord;
}
