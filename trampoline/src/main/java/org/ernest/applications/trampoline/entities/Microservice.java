package org.ernest.applications.trampoline.entities;

import lombok.Data;

@Data
public class Microservice {

	private String id;
	private String name;
	private String pomLocation;
	private String gitLocation;
	private String defaultPort;
	private String actuatorPrefix;
	private String vmArguments;
	private BuildTools buildTool;
	private Float version;
}
