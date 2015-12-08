package org.ernest.applications.trampoline.services;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.entities.Instance;
import org.ernest.applications.trampoline.entities.Microservice;
import org.jboss.resteasy.client.ClientRequest;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Component
public class FileManager {

	private static final String TRAMPOLINE_FOLDER = "/Users/"+System.getProperties().getProperty("user.name")+"/Documents/trampoline";
	private static final String TRAMPOLINE_SETTINGS_PATH = TRAMPOLINE_FOLDER + "/settings.txt";

	public Ecosystem getEcosystem() throws IOException {
		checkIfFileExistsAndCreatedIfNeeded();
		return new Gson().fromJson(FileUtils.readFileToString(new File(TRAMPOLINE_SETTINGS_PATH)), Ecosystem.class);
	}
	
	public void setMavenLocation(String path) throws JsonSyntaxException, IOException {
		Ecosystem ecosystem = new Gson().fromJson(FileUtils.readFileToString(new File(TRAMPOLINE_SETTINGS_PATH)), Ecosystem.class);
		ecosystem.setMavenLocation(path);
		FileUtils.writeStringToFile(new File(TRAMPOLINE_SETTINGS_PATH), new Gson().toJson(ecosystem));
	}
	
	public void setNewMicroservice(String name, String pomLocation, String defaultPort) throws IOException {
		Microservice microservice = new Microservice();
		microservice.setId(UUID.randomUUID().toString());
		microservice.setName(name);
		microservice.setPomLocation(pomLocation);
		microservice.setDefaultPort(defaultPort);
		FileUtils.writeStringToFile(new File(TRAMPOLINE_FOLDER +"/"+ microservice.getId() +".sh"), "export M2_HOME=$1; export PATH=$PATH:$M2_HOME/bin; cd " + pomLocation + "; mvn spring-boot:run -Dserver.port=$2;");
		
		Ecosystem ecosystem = new Gson().fromJson(FileUtils.readFileToString(new File(TRAMPOLINE_SETTINGS_PATH)), Ecosystem.class);
		ecosystem.getMicroservices().add(microservice);
		FileUtils.writeStringToFile(new File(TRAMPOLINE_SETTINGS_PATH), new Gson().toJson(ecosystem));
	}
	
	public void removeMicroservice(String id) throws JsonSyntaxException, IOException {
		Ecosystem ecosystem = new Gson().fromJson(FileUtils.readFileToString(new File(TRAMPOLINE_SETTINGS_PATH)), Ecosystem.class);
		ecosystem.setMicroservices(ecosystem.getMicroservices().stream().filter(m -> !m.getId().equals(id)).collect(Collectors.toList()));
		
		FileUtils.writeStringToFile(new File(TRAMPOLINE_SETTINGS_PATH), new Gson().toJson(ecosystem));
	}

	public void startInstance(String id, String port) throws IOException {
		Ecosystem ecosystem = new Gson().fromJson(FileUtils.readFileToString(new File(TRAMPOLINE_SETTINGS_PATH)), Ecosystem.class);
		
		Microservice microservice = ecosystem.getMicroservices().stream().filter(m -> m.getId().equals(id)).collect(Collectors.toList()).get(0);
		
		new ProcessBuilder("sh", TRAMPOLINE_FOLDER + "/" + microservice.getId() + ".sh", ecosystem.getMavenLocation(), port).start();
		
		Instance instance = new Instance();
		instance.setId(UUID.randomUUID().toString());
		instance.setPort(port);
		instance.setName(microservice.getName());
		instance.setPomLocation(microservice.getPomLocation());
		ecosystem.getInstances().add(instance);
		
		FileUtils.writeStringToFile(new File(TRAMPOLINE_SETTINGS_PATH), new Gson().toJson(ecosystem));
	}

	public void killInstance(String id) throws Exception {
		Ecosystem ecosystem = new Gson().fromJson(FileUtils.readFileToString(new File(TRAMPOLINE_SETTINGS_PATH)), Ecosystem.class);
		Instance instance = ecosystem.getInstances().stream().filter(i -> i.getId().equals(id)).collect(Collectors.toList()).get(0);
		
		new ClientRequest("http://localhost:" + instance.getPort() + "/shutdown").post(String.class);
		
		ecosystem.setInstances(ecosystem.getInstances().stream().filter(i -> !i.getId().equals(id)).collect(Collectors.toList()));
		FileUtils.writeStringToFile(new File(TRAMPOLINE_SETTINGS_PATH), new Gson().toJson(ecosystem));
	}

	public String getStatusInstance(String id) throws JsonSyntaxException, IOException {
		Ecosystem ecosystem = new Gson().fromJson(FileUtils.readFileToString(new File(TRAMPOLINE_SETTINGS_PATH)), Ecosystem.class);
		Instance instance = ecosystem.getInstances().stream().filter(i -> i.getId().equals(id)).collect(Collectors.toList()).get(0);
		try{
			new ClientRequest("http://localhost:" + instance.getPort() + "/env").get(String.class);
		}catch(Exception e){
			return "not deployed";
		}
		return "deployed";
	}

	public void removeNotDeployedInstances() throws JsonSyntaxException, IOException {
		Ecosystem ecosystem = new Gson().fromJson(FileUtils.readFileToString(new File(TRAMPOLINE_SETTINGS_PATH)), Ecosystem.class);
		ecosystem.setInstances(ecosystem.getInstances().stream().filter(i -> isDeployed(i)).collect(Collectors.toList()));
		FileUtils.writeStringToFile(new File(TRAMPOLINE_SETTINGS_PATH), new Gson().toJson(ecosystem));
	}
	
	private void checkIfFileExistsAndCreatedIfNeeded() throws IOException {
		File file = new File(TRAMPOLINE_FOLDER);
		if(!file.exists()){			
			file.mkdirs();
			FileUtils.writeStringToFile(new File(TRAMPOLINE_SETTINGS_PATH), new Gson().toJson(new Ecosystem()));
		}
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
