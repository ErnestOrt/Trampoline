package org.ernest.applications.trampoline.entities;

import lombok.Data;

@Data
public class Instance {

	private String id;
	private String ip;
	private String name;
	private String pomLocation;
	private String port;
	private String actuatorPrefix;
	private String vmArguments;
	private String microserviceId;

}
