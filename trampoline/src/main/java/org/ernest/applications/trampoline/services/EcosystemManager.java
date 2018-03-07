package org.ernest.applications.trampoline.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.ernest.applications.trampoline.entities.*;
import org.ernest.applications.trampoline.exceptions.CreatingMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.ernest.applications.trampoline.exceptions.RunningMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.SavingEcosystemException;
import org.ernest.applications.trampoline.exceptions.ShuttingDownInstanceException;
import org.ernest.applications.trampoline.utils.PortsChecker;
import org.jboss.resteasy.client.ClientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EcosystemManager {
	
	@Autowired
	FileManager fileManager;
	
	public Ecosystem getEcosystem() throws CreatingSettingsFolderException, ReadingEcosystemException {
		return fileManager.getEcosystem();
	}
	
	public void setMavenBinaryLocation(String path) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException {
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setMavenBinaryLocation(path);
		fileManager.saveEcosystem(ecosystem);
	}
	
	public void setMavenHomeLocation(String path) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException {
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setMavenHomeLocation(path);
		fileManager.saveEcosystem(ecosystem);
	}
	
	public void setNewMicroservice(String name, String pomLocation, String defaultPort, String actuatorPrefix, String vmArguments, String buildTool, String gitLocation) throws CreatingSettingsFolderException, ReadingEcosystemException, CreatingMicroserviceScriptException, SavingEcosystemException {
		Ecosystem ecosystem = fileManager.getEcosystem();
		
		Microservice microservice = new Microservice();
		microservice.setId(UUID.randomUUID().toString());
		microservice.setName(name);
		microservice.setPomLocation(pomLocation);
		microservice.setDefaultPort(defaultPort);
		microservice.setActuatorPrefix(actuatorPrefix);
		microservice.setVmArguments(vmArguments);
		microservice.setBuildTool(BuildTools.getByCode(buildTool));
		microservice.setGitLocation(gitLocation);

		ecosystem.getMicroservices().add(microservice);
		fileManager.saveEcosystem(ecosystem);
	}
	
	public void removeMicroservice(String idToBeDeleted) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException {
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setMicroservices(ecosystem.getMicroservices().stream().filter(m -> !m.getId().equals(idToBeDeleted)).collect(Collectors.toList()));
		ecosystem.getMicroservicesGroups().forEach(g-> g.setMicroservicesIds(g.getMicroservicesIds().stream().filter(id -> !id.equals(idToBeDeleted)).collect(Collectors.toList())));
		fileManager.saveEcosystem(ecosystem);
	}

	public void setMicroserviceGroup(String name, List<String> idsMicroservicesGroup) {
		MicroservicesGroup microservicesGroup = new MicroservicesGroup();
		microservicesGroup.setId(UUID.randomUUID().toString());
		microservicesGroup.setName(name);
		microservicesGroup.setMicroservicesIds(idsMicroservicesGroup);

		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.getMicroservicesGroups().add(microservicesGroup);
		fileManager.saveEcosystem(ecosystem);
	}

	public void removeGroup(String id) {
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setMicroservicesGroups(ecosystem.getMicroservicesGroups().stream().filter(g -> !g.getId().equals(id)).collect(Collectors.toList()));
		fileManager.saveEcosystem(ecosystem);
	}

	public void startInstance(String id, String port, String vmArguments) throws CreatingSettingsFolderException, ReadingEcosystemException, RunningMicroserviceScriptException, SavingEcosystemException{
		Ecosystem ecosystem = fileManager.getEcosystem();
		
		Microservice microservice = ecosystem.getMicroservices().stream().filter(m -> m.getId().equals(id)).findAny().get();
		fileManager.runMicroservice(microservice, ecosystem.getMavenBinaryLocation(), ecosystem.getMavenHomeLocation(), port, vmArguments);
		
		Instance instance = new Instance();
		instance.setId(UUID.randomUUID().toString());
		instance.setPort(port);
		instance.setName(microservice.getName());
		instance.setPomLocation(microservice.getPomLocation());
		instance.setActuatorPrefix(microservice.getActuatorPrefix());
		instance.setVmArguments(vmArguments);
		instance.setMicroserviceId(id);
		ecosystem.getInstances().add(instance);
		fileManager.saveEcosystem(ecosystem);
	}

	public void killInstance(String id) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException, ShuttingDownInstanceException {
		Ecosystem ecosystem = fileManager.getEcosystem();
		Instance instance = ecosystem.getInstances().stream().filter(i -> i.getId().equals(id)).collect(Collectors.toList()).get(0);
		
		try {
			new ClientRequest("http://localhost:" + instance.getPort() + instance.getActuatorPrefix() + "/shutdown").post(String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ecosystem.setInstances(ecosystem.getInstances().stream().filter(i -> !i.getId().equals(id)).collect(Collectors.toList()));
		fileManager.saveEcosystem(ecosystem);
	}

	public String getStatusInstance(String id) throws CreatingSettingsFolderException, ReadingEcosystemException {
		Ecosystem ecosystem = fileManager.getEcosystem();
		List<Instance> instances = ecosystem.getInstances().stream().filter(i -> i.getId().equals(id)).collect(Collectors.toList());
		if(!instances.isEmpty() && isDeployed(instances.get(0))){
			return StatusInstance.DEPLOYED.getCode();
		}
		return StatusInstance.NOT_DEPLOYED.getCode();
	}

	private boolean isDeployed(Instance instance) {
		try{
			new ClientRequest("http://localhost:" + instance.getPort() + instance.getActuatorPrefix() + "/env").get(String.class);
		}catch(Exception e){
			return false;
		}
		return true;
	}

	public void startGroup(String id) {
		MicroservicesGroup group = fileManager.getEcosystem().getMicroservicesGroups().stream().filter(g -> g.getId().equals(id)).findFirst().get();

		fileManager.getEcosystem().getMicroservices().stream()
													 .filter(m->group.getMicroservicesIds().contains(m.getId()))
													 .forEach(m->prepareMicroservice(m));
	}

	private void prepareMicroservice(Microservice microservice) {
		int port = Integer.parseInt(microservice.getDefaultPort());
		boolean instanceStarted = false;
		List<Instance> instances = fileManager.getEcosystem().getInstances();

		while(!instanceStarted) {
			final int portToBeLaunched = port;
			if (PortsChecker.available(portToBeLaunched) && !instances.stream().anyMatch(i -> i.getPort().equals(String.valueOf(portToBeLaunched)))) {
				startInstance(microservice.getId(), String.valueOf(port), "");
				instanceStarted = true;
			}else{
				port++;
			}
		}
	}

	public void updateMicroservice(String id, String pomLocation, String defaultPort, String actuatorPrefix, String vmArguments, String gitLocation) {
		Ecosystem ecosystem = fileManager.getEcosystem();

		Microservice microservice = ecosystem.getMicroservices().stream().filter(m -> m.getId().equals(id)).findAny().get();
		microservice.setPomLocation(pomLocation);
		microservice.setDefaultPort(defaultPort);
		microservice.setActuatorPrefix(actuatorPrefix);
		microservice.setVmArguments(vmArguments);
		microservice.setGitLocation(gitLocation);

		fileManager.saveEcosystem(ecosystem);
	}

	public void restartInstance(String instanceId) {
		Ecosystem ecosystem = fileManager.getEcosystem();
		Instance instance = ecosystem.getInstances().stream().filter(i -> i.getId().equals(instanceId)).findFirst().get();
		killInstance(instance.getId());
		startInstance(instance.getMicroserviceId(), instance.getPort(), instance.getVmArguments());
	}

	public void saveGitCred(String user, String pass) {
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setGitCredentials(new GitCredentials(user, pass));
		fileManager.saveEcosystem(ecosystem);
	}

	public void cleanGitCred() {
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setGitCredentials(null);
		fileManager.saveEcosystem(ecosystem);
	}
}
