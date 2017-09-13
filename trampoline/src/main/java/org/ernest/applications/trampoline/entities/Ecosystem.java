package org.ernest.applications.trampoline.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;


@Data
public class Ecosystem {

	private String mavenBinaryLocation;
	private String mavenHomeLocation;
	private List<Microservice> microservices;
	private List<Instance> instances;
	private List<MicroservicesGroup> microservicesGroups;

	public Ecosystem() {
		microservices = new ArrayList<>();
		instances = new ArrayList<>();
		microservicesGroups = new ArrayList<>();
	}
}
