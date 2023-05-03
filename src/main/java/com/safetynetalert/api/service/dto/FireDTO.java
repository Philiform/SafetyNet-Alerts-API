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
public class FireDTO {

	private List<PersonFireFloodDTO> persons;

	@ArraySchema(arraySchema = @Schema(example = "[\"8\", \"12\"]"))
	private List<String> firestationNumber;
}
