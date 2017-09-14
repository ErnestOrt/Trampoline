package org.ernest.applications.trampoline.entities;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Data;


@Data
public class Ecosystem {

	private String mavenBinaryLocation;
	private String mavenHomeLocation;
	private Collection<Microservice> microservices;
	private Collection<Instance> instances;
	private Collection<MicroservicesGroup> microservicesGroups;

	public Ecosystem() {
		microservices = new ArrayList<>();
		instances = new ArrayList<>();
		microservicesGroups = new ArrayList<>();
	}
}
