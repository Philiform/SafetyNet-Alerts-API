package com.safetynetalert.api.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildAlertDTO {

	private String firstName;

	private String lastName;

	private String age;

	private List<ChildFamilyDTO> family;
}
