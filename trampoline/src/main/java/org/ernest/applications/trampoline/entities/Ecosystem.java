package org.ernest.applications.trampoline.entities;

import java.util.ArrayList;
import java.util.List;

public class Ecosystem {

	private GitCredentials gitCredentials;
	private String mavenBinaryLocation;
	private String mavenHomeLocation;
	private List<Microservice> microservices;
	private List<ExternalInstance> externalInstances;
	private List<Instance> instances;
	private List<MicroservicesGroup> microservicesGroups;
	
	public Ecosystem() {
		microservices = new ArrayList<>();
		instances = new ArrayList<>();
		microservicesGroups = new ArrayList<>();
		externalInstances = new ArrayList<>();
		gitCredentials = new GitCredentials();
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

	public List<MicroservicesGroup> getMicroservicesGroups() {
		return microservicesGroups;
	}

	public void setMicroservicesGroups(List<MicroservicesGroup> microservicesGroups) {
		this.microservicesGroups = microservicesGroups;
	}

	public GitCredentials getGitCredentials() {
		return gitCredentials;
	}

	public void setGitCredentials(GitCredentials gitCredentials) {
		this.gitCredentials = gitCredentials;
	}

	public List<ExternalInstance> getExternalInstances() {
		return externalInstances;
	}

	public void setExternalInstances(List<ExternalInstance> externalInstances) {
		this.externalInstances = externalInstances;
	}
}
