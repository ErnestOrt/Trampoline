package org.ernest.applications.trampoline.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Microservice {

	private String id;
	private String name;
	private String pomLocation;
	private String gitLocation;
	private String defaultPort;
	private String actuatorPrefix;
	private String vmArguments;
	private String dependsOn;
	private BuildTools buildTool;
	private Float version;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPomLocation() {
		return pomLocation;
	}
	public void setPomLocation(String pomLocation) {
		this.pomLocation = pomLocation;
	}
	public String getDefaultPort() {
		return defaultPort;
	}
	public void setDefaultPort(String defaultPort) {
		this.defaultPort = defaultPort;
	}
	public String getActuatorPrefix() {
		return actuatorPrefix;
	}
	public String getVmArguments() {
		return vmArguments;
	}
	public void setVmArguments(String vmArguments) {
		this.vmArguments = vmArguments;
	}
	public void setActuatorPrefix(String actuatorPrefix) {
		this.actuatorPrefix = actuatorPrefix;
	}
	public String getDependsOn() {
		return dependsOn;
	}
	@JsonIgnore
	public List<String> getDependsOnList() {
		return Arrays.stream(dependsOn.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
	}
	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn;
	}

	public BuildTools getBuildTool() {
		return buildTool;
	}

	public void setBuildTool(BuildTools buildTool) {
		this.buildTool = buildTool;
	}

	public Float getVersion() {
		return version;
	}

	public void setVersion(Float version) {
		this.version = version;
	}

	public String getGitLocation() {
		return gitLocation;
	}

	public void setGitLocation(String gitLocation) {
		this.gitLocation = gitLocation;
	}

	@Override
	public String toString() {
		return "Microservice{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", pomLocation='" + pomLocation + '\'' +
				", gitLocation='" + gitLocation + '\'' +
				", defaultPort='" + defaultPort + '\'' +
				", actuatorPrefix='" + actuatorPrefix + '\'' +
				", vmArguments='" + vmArguments + '\'' +
				", dependsOn='" + dependsOn + '\'' +
				", buildTool=" + buildTool +
				", version=" + version +
				'}';
	}

}