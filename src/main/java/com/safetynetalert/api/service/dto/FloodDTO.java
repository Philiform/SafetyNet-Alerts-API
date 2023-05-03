package com.safetynetalert.api.service.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FloodDTO {

	@Schema(example = "1509 Culver St")
	private String address;

	private List<PersonFireFloodDTO> persons;
}
