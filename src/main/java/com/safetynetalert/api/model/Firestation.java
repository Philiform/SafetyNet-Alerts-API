package com.safetynetalert.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firestation {

	@Size(min = 3, max = 50, message = "address must be between 3 and 50 characters")
	@NotNull
	@Schema(example = "1509 Culver St")
	private String address;

	@Size(min = 1, max = 4, message = "station must be between 1 and 4 characters")
	@Schema(example = "12")
	@NotNull
	private String station;

	public Firestation(final Firestation f) {
		this.address = f.address;
		this.station = f.station;
	}

}
