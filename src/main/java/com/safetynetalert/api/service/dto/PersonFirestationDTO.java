package com.safetynetalert.api.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonFirestationDTO {

	private String firstName;

	private String lastName;

	private String address;

	private String phone;
}
