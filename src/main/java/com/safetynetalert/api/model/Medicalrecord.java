package com.safetynetalert.api.model;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicalrecord {

	@NotNull
	@Size(min = 2, max = 50, message = "firstName must be between 2 and 50 characters")
	private String firstName;

	@NotNull
	@Size(min = 2, max = 50, message = "lastName must be between 2 and 50 characters")
	private String lastName;

	@NotNull
	@Size(min = 10, max = 10, message = "birthdate must be mm/dd/yyyy")
	private String birthdate;

	private List<String> medications;

	private List<String> allergies;

	public Medicalrecord(final Medicalrecord m) {
		this.firstName = m.firstName;
		this.lastName = m.lastName;
		this.birthdate = m.birthdate;
		this.medications = m.medications;
		this.allergies = m.allergies;
	}
}
