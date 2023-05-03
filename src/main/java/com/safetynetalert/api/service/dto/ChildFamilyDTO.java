package com.safetynetalert.api.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildFamilyDTO {

	@Schema(example = "John")
	private String firstName;

	@Schema(example = "Boyd")
	private String lastName;
}
