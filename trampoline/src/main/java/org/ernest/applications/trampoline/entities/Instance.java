package org.ernest.applications.trampoline.entities;

public class Instance {

	private String id;
	private String ip;
	private String name;
	private String pomLocation;
	private String port;
	private String actuatorPrefix;
	private String vmArguments;
	private String microserviceId;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getActuatorPrefix() {
		return actuatorPrefix;
	}
	public void setActuatorPrefix(String actuatorPrefix) {
		this.actuatorPrefix = actuatorPrefix;
	}
	public String getVmArguments() {
		return vmArguments;
	}
	public void setVmArguments(String vmArguments) {
		this.vmArguments = vmArguments;
	}

	public String getMicroserviceId() {
		return microserviceId;
	}

	public void setMicroserviceId(String microserviceId) {
		this.microserviceId = microserviceId;
	}

	@Override
	public String toString() {
		return "Instance{" +
				"id='" + id + '\'' +
				", ip='" + ip + '\'' +
				", name='" + name + '\'' +
				", pomLocation='" + pomLocation + '\'' +
				", port='" + port + '\'' +
				", actuatorPrefix='" + actuatorPrefix + '\'' +
				", vmArguments='" + vmArguments + '\'' +
				", microserviceId='" + microserviceId + '\'' +
				'}';
	}
}
