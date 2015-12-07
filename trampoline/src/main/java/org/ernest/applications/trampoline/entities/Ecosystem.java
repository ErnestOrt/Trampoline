package org.ernest.applications.trampoline.entities;

import java.util.ArrayList;
import java.util.List;

public class Ecosystem {

	private String mavenLocation;
	private List<Microservice> microservices;
	private List<Instance> instances;
	
	public Ecosystem() {
		microservices = new ArrayList<Microservice>();
		instances = new ArrayList<Instance>();
	}

	public String getMavenLocation() {
		return mavenLocation;
	}
	
	public void setMavenLocation(String mavenLocation) {
		this.mavenLocation = mavenLocation;
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
