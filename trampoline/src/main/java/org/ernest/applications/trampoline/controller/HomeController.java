package org.ernest.applications.trampoline.controller;

import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.exceptions.CreatingMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.ernest.applications.trampoline.exceptions.RunningMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.SavingEcosystemException;
import org.ernest.applications.trampoline.exceptions.ShuttingDownInstanceException;
import org.ernest.applications.trampoline.services.EcosystemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	private static final String HOME_VIEW = "home";
	
	@Autowired
	EcosystemManager ecosystemManager;

	@RequestMapping("/trampoline")
    public String greeting(Model model) throws CreatingSettingsFolderException, ReadingEcosystemException{
		Ecosystem ecosystem = ecosystemManager.getEcosystem();
		model.addAttribute("microservices", ecosystem.getMicroservices());
		model.addAttribute("instances", ecosystem.getInstances());
		model.addAttribute("mavenBinaryLocation", ecosystem.getMavenBinaryLocation());
		model.addAttribute("mavenHomeLocation", ecosystem.getMavenHomeLocation());
		model.addAttribute("mavenBinaryLocationMessage", ecosystem.getMavenBinaryLocation() == null ? "Please set maven Binary Location. Ex: /Users/ernest/Documents/workspace/tools/apache-maven-3.2.1" : ecosystem.getMavenBinaryLocation());
		model.addAttribute("mavenHomeLocationMessage", ecosystem.getMavenHomeLocation() == null ? "Please set maven Home Location. Ex: /Users/ernest/Documents/workspace/tools/apache-maven-3.2.1" : ecosystem.getMavenHomeLocation());
		return HOME_VIEW;
    }
	
	@RequestMapping(value= "/setmavenbinarylocation", method = RequestMethod.POST)
	@ResponseBody
    public void setMavenBinaryLocation(@RequestParam(value="path") String path) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException {
		ecosystemManager.setMavenBinaryLocation(path);
    }
	
	@RequestMapping(value= "/setmavenhomelocation", method = RequestMethod.POST)
	@ResponseBody
    public void setMavenHomeLocation(@RequestParam(value="path") String path) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException {
		ecosystemManager.setMavenHomeLocation(path);
    }
	
	@RequestMapping(value= "/setnewmicroservice", method = RequestMethod.POST)
	@ResponseBody
    public void setNewMicroservice(@RequestParam(value="name") String name, @RequestParam(value="pomLocation") String pomLocation, 
    		@RequestParam(value="defaultPort") String defaultPort, @RequestParam(value="actuatorPrefix") String actuatorPrefix, 
    		@RequestParam(value="vmArguments") String vmArguments) 
    				throws CreatingSettingsFolderException, ReadingEcosystemException, CreatingMicroserviceScriptException, SavingEcosystemException {
		ecosystemManager.setNewMicroservice(name, pomLocation, defaultPort, actuatorPrefix, vmArguments);
    }
	
	@RequestMapping(value= "/removemicroservice", method = RequestMethod.POST)
	@ResponseBody
    public void removeMicroservice(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException{
		ecosystemManager.removeMicroservice(id);
    }
	
	@RequestMapping(value= "/startinstance", method = RequestMethod.POST)
	@ResponseBody
    public void startInstance(@RequestParam(value="id") String id, @RequestParam(value="port") String port, @RequestParam(value="vmArguments") String vmArguments) throws CreatingSettingsFolderException, ReadingEcosystemException, RunningMicroserviceScriptException, SavingEcosystemException {
		ecosystemManager.startInstance(id, port, vmArguments);
    }
	
	@RequestMapping(value= "/health", method = RequestMethod.POST)
	@ResponseBody
    public String checkStatusInstance(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException {
		return ecosystemManager.getStatusInstance(id);
    }
	
	@RequestMapping(value= "/killinstance", method = RequestMethod.POST)
	@ResponseBody
    public void killInstance(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException, ShuttingDownInstanceException {
		ecosystemManager.killInstance(id);
    }
	
	@RequestMapping(value= "/removenotdeployedinstances", method = RequestMethod.POST)
	@ResponseBody
    public void removeNotDeployedInstances() throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException {
		ecosystemManager.removeNotDeployedInstances();
    }
}
