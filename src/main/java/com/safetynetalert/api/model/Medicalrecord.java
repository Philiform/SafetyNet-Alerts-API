package com.safetynetalert.api.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicalrecord {

	@Size(min = 2, max = 50, message = "firstName must be between 2 and 50 characters")
	@Schema(example = "John")
	@NotNull
	private String firstName;

	@Size(min = 2, max = 50, message = "lastName must be between 2 and 50 characters")
	@Schema(example = "Boyd")
	@NotNull
	private String lastName;

	@Size(min = 10, max = 10, message = "birthdate must be mm/dd/yyyy")
	@Schema(example = "03/06/1984")
	@NotNull
	private String birthdate;

	@ArraySchema(arraySchema = @Schema(example = "[\"hydrapermazol:900mg\", \"pharmacol:5000mg\"]"))
	private List<String> medications;

	@ArraySchema(arraySchema = @Schema(example = "[\"illisoxian\", \"peanut\"]"))
	private List<String> allergies;

	public Medicalrecord(final Medicalrecord m) {
		this.firstName = m.firstName;
		this.lastName = m.lastName;
		this.birthdate = m.birthdate;
		this.medications = m.medications;
		this.allergies = m.allergies;
	}
}
