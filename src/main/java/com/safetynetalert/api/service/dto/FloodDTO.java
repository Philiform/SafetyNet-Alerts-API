package com.safetynetalert.api.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FloodDTO {

	private String address;

	private List<PersonFireFloodDTO> persons;
}
