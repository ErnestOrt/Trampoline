package org.ernest.applications.trampoline.entities;

import lombok.Getter;

@Getter
public enum StatusInstance {

	DEPLOYED("deployed"),
	NOT_DEPLOYED("not deployed");
	
	private String code;
	
	StatusInstance(String code){
		this.code = code;
	}
	
}