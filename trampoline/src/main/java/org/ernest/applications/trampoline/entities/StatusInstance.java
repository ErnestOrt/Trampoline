package org.ernest.applications.trampoline.entities;

public enum StatusInstance {

	DEPLOYED("deployed"),
	NOT_DEPLOYED("not deployed");
	
	private String code;
	
	StatusInstance(String code){
		this.code = code;
	}
	
	public String getCode(){
		return code;
	}
}