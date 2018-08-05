package org.ernest.applications.trampoline.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.ernest.applications.trampoline.collectors.TraceCollector;
import org.ernest.applications.trampoline.entities.*;
import org.ernest.applications.trampoline.entities.GitCredentials.SshSettings;
import org.ernest.applications.trampoline.exceptions.CreatingMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.ernest.applications.trampoline.exceptions.RunningMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.SavingEcosystemException;
import org.ernest.applications.trampoline.exceptions.ShuttingDownInstanceException;
import org.ernest.applications.trampoline.utils.PortsChecker;
import org.jboss.resteasy.client.ClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EcosystemManager {

	private static final Logger log = LoggerFactory.getLogger(TraceCollector.class);

	@Autowired
	FileManager fileManager;
	
	public Ecosystem getEcosystem() throws CreatingSettingsFolderException, ReadingEcosystemException {
		return fileManager.getEcosystem();
	}
	
	public void setMavenBinaryLocation(String path) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException {
		log.info("Saving Maven Binary Location path [{}]", path);
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setMavenBinaryLocation(path);
		fileManager.saveEcosystem(ecosystem);
	}
	
	public void setMavenHomeLocation(String path) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException {
		log.info("Saving Maven Home Location path [{}]", path);
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setMavenHomeLocation(path);
		fileManager.saveEcosystem(ecosystem);
	}
	
	public void setNewMicroservice(String name, String pomLocation, String defaultPort, String actuatorPrefix, String vmArguments, String buildTool, String gitLocation) throws CreatingSettingsFolderException, ReadingEcosystemException, CreatingMicroserviceScriptException, SavingEcosystemException {
		Ecosystem ecosystem = fileManager.getEcosystem();

		log.info("Creating new microservice name: [{}]", name);
		Microservice microservice = new Microservice();
		microservice.setId(UUID.randomUUID().toString());
		microservice.setName(name);
		microservice.setPomLocation(pomLocation);
		microservice.setDefaultPort(defaultPort);
		microservice.setActuatorPrefix(actuatorPrefix);
		microservice.setVmArguments(vmArguments);
		microservice.setBuildTool(BuildTools.getByCode(buildTool));
		microservice.setGitLocation(gitLocation);
		fileManager.createScript(microservice);

		log.info("Saving microservice: [{}]", microservice.toString());
		ecosystem.getMicroservices().add(microservice);
		fileManager.saveEcosystem(ecosystem);
	}
	
	public void removeMicroservice(String idToBeDeleted) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException {
		log.info("Removing microservice id: [{}]", idToBeDeleted);
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setMicroservices(ecosystem.getMicroservices().stream().filter(m -> !m.getId().equals(idToBeDeleted)).collect(Collectors.toList()));
		ecosystem.getMicroservicesGroups().forEach(g-> g.setMicroservicesIds(g.getMicroservicesIds().stream().filter(id -> !id.equals(idToBeDeleted)).collect(Collectors.toList())));
		fileManager.saveEcosystem(ecosystem);
	}

	public void setMicroserviceGroup(String name, List<String> idsMicroservicesGroup, List<Integer> delaysMicroservicesGroup) {
		log.info("Creating group name: [{}] with microservices [{}]", name, idsMicroservicesGroup.stream().collect(Collectors.joining(",")));
		MicroservicesGroup microservicesGroup = new MicroservicesGroup();
		microservicesGroup.setId(UUID.randomUUID().toString());
		microservicesGroup.setName(name);
		microservicesGroup.setMicroservicesIds(idsMicroservicesGroup);
		microservicesGroup.setMicroservicesDelays(delaysMicroservicesGroup);

		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.getMicroservicesGroups().add(microservicesGroup);
		fileManager.saveEcosystem(ecosystem);
	}

	public void removeGroup(String id) {
		log.info("Removing group id: [{}]", id);
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setMicroservicesGroups(ecosystem.getMicroservicesGroups().stream().filter(g -> !g.getId().equals(id)).collect(Collectors.toList()));
		fileManager.saveEcosystem(ecosystem);
	}

	public void startInstance(String id, String port, String vmArguments, Integer startingDelay) throws CreatingSettingsFolderException, ReadingEcosystemException, RunningMicroserviceScriptException, SavingEcosystemException, InterruptedException {
		log.info("Starting instances id: [{}] port: [{}] vmArguments: [{}] startingDelay: [{}]", id, port, vmArguments, startingDelay);
		Ecosystem ecosystem = fileManager.getEcosystem();
		
		Microservice microservice = ecosystem.getMicroservices().stream().filter(m -> m.getId().equals(id)).findAny().get();
		Thread.sleep(startingDelay*1000);
		log.info("Launching script to start instances id: [{}]", id);
		fileManager.runScript(microservice, ecosystem.getMavenBinaryLocation(), ecosystem.getMavenHomeLocation(), port, vmArguments);
		
		Instance instance = new Instance();
		instance.setId(UUID.randomUUID().toString());
		instance.setIp("127.0.0.1");
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
		log.info("Removing instance id: [{}]", id);

		Ecosystem ecosystem = fileManager.getEcosystem();
		Instance instance = ecosystem.getInstances().stream().filter(i -> i.getId().equals(id)).collect(Collectors.toList()).get(0);

		if (instance.getIp().equals("127.0.0.1")) {
			log.info("Stopping instance id: [{}]", id);
			try {
				new ClientRequest("http://"+instance.getIp()+":" + instance.getPort() + instance.getActuatorPrefix() + "/shutdown").post(String.class);
			} catch (Exception e) {
				log.error("Stopping instance id: [{}]", id);
			}
		}

		ecosystem.setInstances(ecosystem.getInstances().stream().filter(i -> !i.getId().equals(id)).collect(Collectors.toList()));
		fileManager.saveEcosystem(ecosystem);
	}

	public String getStatusInstance(String id) throws CreatingSettingsFolderException, ReadingEcosystemException {
		log.info("Checking status instances id: [{}]", id);
		Ecosystem ecosystem = fileManager.getEcosystem();
		List<Instance> instances = ecosystem.getInstances().stream().filter(i -> i.getId().equals(id)).collect(Collectors.toList());
		if(!instances.isEmpty() && isDeployed(instances.get(0))){
			return StatusInstance.DEPLOYED.getCode();
		}
		return StatusInstance.NOT_DEPLOYED.getCode();
	}

	private boolean isDeployed(Instance instance) {
		try{
			new ClientRequest("http://"+instance.getIp()+":" + instance.getPort() + instance.getActuatorPrefix() + "/env").get(String.class);
		}catch(Exception e){
			return false;
		}
		return true;
	}

	public void startGroup(String id) throws InterruptedException {
		log.info("Starting group id: [{}]", id);
		Ecosystem ecosystem = fileManager.getEcosystem();
		MicroservicesGroup group = ecosystem.getMicroservicesGroups().stream().filter(g -> g.getId().equals(id)).findFirst().get();

		for(int index = 0; index < group.getMicroservicesIds().size(); index++){
			int microserviceIndex = index;
			Microservice microservice = ecosystem.getMicroservices().stream().filter(m->m.getId().equals(group.getMicroservicesIds().get(microserviceIndex))).findAny().get();
			prepareMicroservice(microservice, group.getMicroservicesDelays().get(microserviceIndex));
		}
	}

	private void prepareMicroservice(Microservice microservice, Integer startingDelay) throws InterruptedException {
		int port = Integer.parseInt(microservice.getDefaultPort());
		boolean instanceStarted = false;
		List<Instance> instances = fileManager.getEcosystem().getInstances();

		while(!instanceStarted) {
			final int portToBeLaunched = port;
			if (PortsChecker.available(portToBeLaunched) && !instances.stream().anyMatch(i -> i.getPort().equals(String.valueOf(portToBeLaunched)))) {
				startInstance(microservice.getId(), String.valueOf(port), "", startingDelay);
				instanceStarted = true;
			}else{
				port++;
			}
		}
	}

	public void updateMicroservice(String id, String pomLocation, String defaultPort, String actuatorPrefix, String vmArguments, String gitLocation) {
		log.info("Updating microservice id: [{}]", id);
		Ecosystem ecosystem = fileManager.getEcosystem();

		Microservice microservice = ecosystem.getMicroservices().stream().filter(m -> m.getId().equals(id)).findAny().get();
		microservice.setPomLocation(pomLocation);
		microservice.setDefaultPort(defaultPort);
		microservice.setActuatorPrefix(actuatorPrefix);
		microservice.setVmArguments(vmArguments);
		microservice.setGitLocation(gitLocation);

		fileManager.createScript(microservice);

		log.info("Saving microservice: [{}]", microservice.toString());
		fileManager.saveEcosystem(ecosystem);
	}

	public void restartInstance(String instanceId) throws InterruptedException {
		log.info("Restarting instance id: [{}]", instanceId);
		Ecosystem ecosystem = fileManager.getEcosystem();
		Instance instance = ecosystem.getInstances().stream().filter(i -> i.getId().equals(instanceId)).findFirst().get();
		killInstance(instance.getId());
		startInstance(instance.getMicroserviceId(), instance.getPort(), instance.getVmArguments(), 0);
	}

	public void saveGitHttpsCred(String user, String pass) {
		log.info("Saving GIT HTTPS Credentials");
		Ecosystem ecosystem = fileManager.getEcosystem();
		GitCredentials gitCredentials = ecosystem.getGitCredentials();
		if (gitCredentials.getHttpsSettings() != null) {
			gitCredentials.getHttpsSettings().setUsername(user);
			gitCredentials.getHttpsSettings().setPass(pass);
		} else {
			ecosystem.setGitCredentials(new GitCredentials(new GitCredentials.HttpsSettings(user, pass)));
		}
		fileManager.saveEcosystem(ecosystem);
	}

	public void saveGitSshCred(String privateKeyLocation, String sshKeyPassword) {
		log.info("Saving GIT SSH Credentials");
		Ecosystem ecosystem = fileManager.getEcosystem();
		GitCredentials gitCredentials = ecosystem.getGitCredentials();
		if (gitCredentials.getSshSettings() != null) {
			gitCredentials.getSshSettings().setSshKeyLocation(privateKeyLocation);
			gitCredentials.getSshSettings().setSshKeyPassword(sshKeyPassword);
		} else {
			ecosystem.setGitCredentials(new GitCredentials(new SshSettings(privateKeyLocation, sshKeyPassword)));
		}
		fileManager.saveEcosystem(ecosystem);
	}

	public void cleanGitCred() {
		log.info("Cleaning GIT Credentials");
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setGitCredentials(new GitCredentials());
		fileManager.saveEcosystem(ecosystem);
	}

    public void setNewExternalInstance(String name, String port, String actuatorPrefix, String ip) {
		Ecosystem ecosystem = fileManager.getEcosystem();

		log.info("Creating new external instance: [{}]", name);
		ExternalInstance externalInstance = new ExternalInstance();
		externalInstance.setId(UUID.randomUUID().toString());
		externalInstance.setName(name);
		externalInstance.setIp(ip);
		externalInstance.setActuatorPrefix(actuatorPrefix);
		externalInstance.setPort(port);

		log.info("Saving external instance: [{}]", externalInstance.toString());
		ecosystem.getExternalInstances().add(externalInstance);
		fileManager.saveEcosystem(ecosystem);
    }

	public void removeExternalInstance(String idToBeDeleted) {
		log.info("Removing microservice id: [{}]", idToBeDeleted);
		Ecosystem ecosystem = fileManager.getEcosystem();
		ecosystem.setExternalInstances(ecosystem.getExternalInstances().stream().filter(i -> !i.getId().equals(idToBeDeleted)).collect(Collectors.toList()));
		fileManager.saveEcosystem(ecosystem);
	}

	public void addExternalInstance(String id) {
		log.info("Adding external instance id: [{}]", id);
		Ecosystem ecosystem = fileManager.getEcosystem();

		ExternalInstance externalInstance = ecosystem.getExternalInstances().stream().filter(i -> i.getId().equals(id)).findAny().get();

		Instance instance = new Instance();
		instance.setId(UUID.randomUUID().toString());
		instance.setIp(externalInstance.getIp());
		instance.setPort(externalInstance.getPort());
		instance.setName(externalInstance.getName());
		instance.setActuatorPrefix(externalInstance.getActuatorPrefix());
		instance.setMicroserviceId(id);
		ecosystem.getInstances().add(instance);
		fileManager.saveEcosystem(ecosystem);
	}
}
