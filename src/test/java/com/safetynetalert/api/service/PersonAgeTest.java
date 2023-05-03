package com.safetynetalert.api.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonAgeTest {

	private PersonAge personAge = new PersonAge();
	private boolean isAdult;
	private int age;

	@BeforeEach
	void setUp() throws Exception {
		isAdult = false;
		age = 0;
	}

	@Test
	void testGivenOld19_WhenGetAge_ThenReturnTrue() {
		// GIVEN
		LocalDate todayMinus19Years = LocalDate.now().minusYears(19);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String date = todayMinus19Years.format(formatter);

		// WHEN
		isAdult = personAge.isAdult(date);

		// THEN
		assertThat(isAdult).isTrue();
	}

	@Test
	void testGivenOld18_WhenGetAge_ThenReturnFalse() {
		// GIVEN
		LocalDate todayMinus19YearsPlus1Day = LocalDate.now().minusYears(19).plusDays(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String date = todayMinus19YearsPlus1Day.format(formatter);

		// WHEN
		isAdult = personAge.isAdult(date);

		// THEN
		assertThat(isAdult).isFalse();
	}

	@Test
	void testGivenDateIs03_06_1985_WhenGetAge_ThenReturn38() {
		// WHEN
		age = personAge.getAge("03/06/1985");

		// THEN
		assertThat(age).isEqualTo(38);
	}

	@Test
	void testGivenDateIs01_14_2020_WhenGetAge_ThenReturn3() {
		// WHEN
		age = personAge.getAge("01/14/2020");

		// THEN
		assertThat(age).isEqualTo(3);
	}
}
