package com.safetynetalert.api.model;

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

	@NotNull
	@Size(min = 2, max = 50, message = "firstName must be between 2 and 50 characters")
	private String firstName;

	@NotNull
	@Size(min = 2, max = 50, message = "lastName must be between 2 and 50 characters")
	private String lastName;

	@NotNull
	@Size(min = 3, max = 50, message = "address must be between 3 and 50 characters")
	private String address;

	@NotNull
	@Size(min = 2, max = 50, message = "city must be between 2 and 50 characters")
	private String city;

	private String zip;

	private String phone;

	@Email(message = "Email should be valid")
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
