package com.safetynetalert.api.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonFirestationDTO {

	@Schema(example = "John")
	private String firstName;

	@Schema(example = "Boyd")
	private String lastName;

	@Schema(example = "1509 Culver St")
	private String address;

	@Schema(example = "841-874-6512")
	private String phone;
}
