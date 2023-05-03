package com.safetynetalert.api.service;

import java.time.LocalDate;
import java.time.Period;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PersonAge {

	final byte YEAR = 2;
	final byte MONTH = 0;
	final byte DAY = 1;

	public boolean isAdult(final String date) {
		if(getAge(date) > 18) {
			return true;
		}
		return false;
	}

	public byte getAge(final String date) {
		String[] d = date.split("/");
		LocalDate today = LocalDate.now();
		LocalDate birthdate = LocalDate.of(Integer.parseInt(d[YEAR]), Integer.parseInt(d[MONTH]), Integer.parseInt(d[DAY]));
		Period period = Period.between(birthdate, today);

		return (byte) period.getYears();
	}
}
