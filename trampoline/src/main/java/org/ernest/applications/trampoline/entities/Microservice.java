package org.ernest.applications.trampoline.entities;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Microservice {
	private String id;
	private String name;
	private String pomLocation;
	private String defaultPort;
	private String actuatorPrefix;
	private String vmArguments;
	private BuildTools buildTool;
}