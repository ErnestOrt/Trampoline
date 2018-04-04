package org.ernest.applications.trampoline.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Instance {

	private String id;
	private String name;
	private String pomLocation;
	private String port;
	private String actuatorPrefix;
	private String vmArguments;
	private String microserviceId;
}
