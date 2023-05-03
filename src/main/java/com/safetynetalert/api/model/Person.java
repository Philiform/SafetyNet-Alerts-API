package com.safetynetalert.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

	@Size(min = 2, max = 50, message = "firstName must be between 2 and 50 characters")
	@Schema(example = "John")
	@NotNull
	private String firstName;

	@Size(min = 2, max = 50, message = "lastName must be between 2 and 50 characters")
	@Schema(example = "Boyd")
	@NotNull
	private String lastName;

	@Size(min = 3, max = 50, message = "address must be between 3 and 50 characters")
	@Schema(example = "1509 Culver St")
	@NotNull
	private String address;

	@Size(min = 2, max = 50, message = "city must be between 2 and 50 characters")
	@Schema(example = "Culver")
	@NotNull
	private String city;

	@Schema(example = "97451")
	private String zip;

	@Schema(example = "841-874-6512")
	private String phone;

	@Email(message = "Email should be valid")
	@Schema(example = "jaboyd@email.com")
	private String email;

	public Person(final Person p) {
		this.firstName = p.firstName;
		this.lastName = p.lastName;
		this.address = p.address;
		this.city = p.city;
		this.zip = p.zip;
		this.phone = p.phone;
		this.email = p.email;
	}
}
