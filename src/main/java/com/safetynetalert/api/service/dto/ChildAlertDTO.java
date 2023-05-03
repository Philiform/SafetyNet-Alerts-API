package com.safetynetalert.api.service.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildAlertDTO {

	@Schema(example = "Roger")
	private String firstName;

	@Schema(example = "Boyd")
	private String lastName;

	@Schema(example = "5")
	private String age;

	private List<ChildFamilyDTO> family;
}
