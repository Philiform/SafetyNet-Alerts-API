package com.safetynetalert.api.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firestation {

	@NotNull
	@Size(min = 3, max = 50, message = "address must be between 3 and 50 characters")
	private String address;

	@NotNull
	@Size(min = 1, max = 4, message = "station must be between 1 and 4 characters")
	private String station;

	public Firestation(final Firestation f) {
		this.address = f.address;
		this.station = f.station;
	}

}
