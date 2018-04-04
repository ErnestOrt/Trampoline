package org.ernest.applications.trampoline.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Ecosystem {

	private GitCredentials gitCredentials;
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
