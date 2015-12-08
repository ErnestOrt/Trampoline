package org.ernest.applications.trampoline.services;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.entities.Instance;
import org.ernest.applications.trampoline.entities.Microservice;
import org.jboss.resteasy.client.ClientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonSyntaxException;

@Component
public class EcosystemManager {
	
	@Autowired
	FileManager fileManager;
	
	public Ecosystem getEcosystem() throws IOException {
		return fileManager.getEcosystem();
	}
	
	public void setMavenLocation(String path) throws JsonSyntaxException, IOException {
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setMavenLocation(path);
		fileManager.saveEcosystem(ecosystem);
	}
	
	public void setNewMicroservice(String name, String pomLocation, String defaultPort) throws IOException {
		Ecosystem ecosystem = fileManager.getEcosystem();
		
		Microservice microservice = new Microservice();
		microservice.setId(UUID.randomUUID().toString());
		microservice.setName(name);
		microservice.setPomLocation(pomLocation);
		microservice.setDefaultPort(defaultPort);
		fileManager.createScript(microservice.getId(), pomLocation);
		
		ecosystem.getMicroservices().add(microservice);
		fileManager.saveEcosystem(ecosystem);
	}
	
	public void removeMicroservice(String id) throws JsonSyntaxException, IOException {
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setMicroservices(ecosystem.getMicroservices().stream().filter(m -> !m.getId().equals(id)).collect(Collectors.toList()));	
		fileManager.saveEcosystem(ecosystem);
	}

	public void startInstance(String id, String port) throws IOException {
		Ecosystem ecosystem = fileManager.getEcosystem();
		
		Microservice microservice = ecosystem.getMicroservices().stream().filter(m -> m.getId().equals(id)).collect(Collectors.toList()).get(0);
		fileManager.runScript(microservice.getId(), ecosystem.getMavenLocation(), port);
		
		Instance instance = new Instance();
		instance.setId(UUID.randomUUID().toString());
		instance.setPort(port);
		instance.setName(microservice.getName());
		instance.setPomLocation(microservice.getPomLocation());
		ecosystem.getInstances().add(instance);
		fileManager.saveEcosystem(ecosystem);
	}

	public void killInstance(String id) throws Exception {
		Ecosystem ecosystem = fileManager.getEcosystem();
		Instance instance = ecosystem.getInstances().stream().filter(i -> i.getId().equals(id)).collect(Collectors.toList()).get(0);
		
		new ClientRequest("http://localhost:" + instance.getPort() + "/shutdown").post(String.class);
		
		ecosystem.setInstances(ecosystem.getInstances().stream().filter(i -> !i.getId().equals(id)).collect(Collectors.toList()));
		fileManager.saveEcosystem(ecosystem);
	}

	public String getStatusInstance(String id) throws JsonSyntaxException, IOException {
		Ecosystem ecosystem = fileManager.getEcosystem();
		Instance instance = ecosystem.getInstances().stream().filter(i -> i.getId().equals(id)).collect(Collectors.toList()).get(0);
		if(isDeployed(instance)){
			return "deployed";
		}
		return "not deployed";
	}

	public void removeNotDeployedInstances() throws JsonSyntaxException, IOException {
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setInstances(ecosystem.getInstances().stream().filter(i -> isDeployed(i)).collect(Collectors.toList()));
		fileManager.saveEcosystem(ecosystem);
	}

	private boolean isDeployed(Instance instance) {
		try{
			new ClientRequest("http://localhost:" + instance.getPort() + "/env").get(String.class);
		}catch(Exception e){
			return false;
		}
		return true;
	}
}