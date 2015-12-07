package org.ernest.applications.trampoline.entities;

public class Microservice {

	private String id;
	private String name;
	private String pomLocation;
	private String defaultPort;
	
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
}
