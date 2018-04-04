package org.ernest.applications.trampoline.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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