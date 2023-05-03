package com.safetynetalert.api.service.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirestationDTO {

	private List<PersonFirestationDTO> persons;

	@Schema(example = "4")
	private long numberAdult;

	@Schema(example = "1")
	private long numberChild;
}
