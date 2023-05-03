package com.safetynetalert.api.service.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonMedicalRecordDTO {

	@ArraySchema(arraySchema = @Schema(example = "[\"hydrapermazol:900mg\", \"pharmacol:5000mg\"]"))
	private List<String> medications;

	@ArraySchema(arraySchema = @Schema(example = "[\"illisoxian\", \"peanut\"]"))
	private List<String> allergies;
}
