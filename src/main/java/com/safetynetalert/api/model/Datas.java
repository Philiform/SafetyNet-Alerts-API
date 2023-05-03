package com.safetynetalert.api.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor
@AllArgsConstructor
public class Datas {

	private List<Person> persons = new ArrayList<>();

	private List<Firestation> firestations = new ArrayList<>();

	private List<Medicalrecord> medicalrecords = new ArrayList<>();

	public Datas(final Datas datas) {
		Set<Person> personsSet = new LinkedHashSet<Person>(datas.persons);
		for(Person p : personsSet) {
			persons.add(new Person(p));
		}

		Set<Firestation> firestationsSet = new LinkedHashSet<Firestation>(datas.firestations);
		for(Firestation f : firestationsSet) {
			firestations.add(new Firestation(f));
		}

		Set<Medicalrecord> medicalrecordsSet = new LinkedHashSet<Medicalrecord>(datas.medicalrecords);
		for(Medicalrecord m : medicalrecordsSet) {
			medicalrecords.add(new Medicalrecord(m));
		}
	}
}
