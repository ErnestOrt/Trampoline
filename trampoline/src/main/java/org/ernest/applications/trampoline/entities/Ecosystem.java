package org.ernest.applications.trampoline.entities;

import java.util.ArrayList;
import java.util.List;

public class Ecosystem {

	private List<Microservice> microservices;
	
	public Ecosystem() {
		microservices = new ArrayList<Microservice>();
	}

	public List<Microservice> getMicroservices() {
		return microservices;
	}

	public void setMicroservices(List<Microservice> microservices) {
		this.microservices = microservices;
	}
}
