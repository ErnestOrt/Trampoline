package org.ernest.applications.trampoline.entities;

import java.util.ArrayList;
import java.util.List;

public class Ecosystem {

	private String mavenBinaryLocation;
	private String mavenHomeLocation;
	private List<Microservice> microservices;
	private List<Instance> instances;
	
	public Ecosystem() {
		microservices = new ArrayList<Microservice>();
		instances = new ArrayList<Instance>();
	}

	public String getMavenBinaryLocation() {
		return mavenBinaryLocation;
	}

	public void setMavenBinaryLocation(String mavenBinaryLocation) {
		this.mavenBinaryLocation = mavenBinaryLocation;
	}

	public String getMavenHomeLocation() {
		return mavenHomeLocation;
	}

	public void setMavenHomeLocation(String mavenHomeLocation) {
		this.mavenHomeLocation = mavenHomeLocation;
	}

	public List<Microservice> getMicroservices() {
		return microservices;
	}

	public void setMicroservices(List<Microservice> microservices) {
		this.microservices = microservices;
	}

	public List<Instance> getInstances() {
		return instances;
	}

	public void setInstances(List<Instance> instances) {
		this.instances = instances;
	}
}
