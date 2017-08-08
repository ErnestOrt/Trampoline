package org.ernest.applications.trampoline.controller;

import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.entities.Metrics;
import org.ernest.applications.trampoline.exceptions.CreatingMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.ernest.applications.trampoline.exceptions.RunningMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.SavingEcosystemException;
import org.ernest.applications.trampoline.exceptions.ShuttingDownInstanceException;
import org.ernest.applications.trampoline.services.EcosystemManager;
import org.ernest.applications.trampoline.services.MetricsCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Queue;

@Controller
public class HomeController {

	private static final String HOME_VIEW = "home";
	
	@Autowired
	EcosystemManager ecosystemManager;

	@Autowired
	MetricsCollector metricsCollector;

	@RequestMapping("/trampoline")
    public String greeting(Model model) throws CreatingSettingsFolderException, ReadingEcosystemException{
		Ecosystem ecosystem = ecosystemManager.getEcosystem();
		model.addAttribute("microservices", ecosystem.getMicroservices());
		model.addAttribute("instances", ecosystem.getInstances());
		model.addAttribute("mavenLocation", ecosystem.getMavenLocation());
		model.addAttribute("mavenLocationMessage", ecosystem.getMavenLocation() == null ? "Please set maven Location. Ex: /Users/ernest/Documents/workspace/tools/apache-maven-3.2.1" : ecosystem.getMavenLocation());
		return HOME_VIEW;
    }
	
	@RequestMapping(value= "/setmavenlocation", method = RequestMethod.POST)
	@ResponseBody
    public void setMavenLocation(@RequestParam(value="path") String path) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException {
		ecosystemManager.setMavenLocation(path);
    }
	
	@RequestMapping(value= "/setnewmicroservice", method = RequestMethod.POST)
	@ResponseBody
    public void setNewMicroservice(@RequestParam(value="name") String name, @RequestParam(value="pomLocation") String pomLocation, @RequestParam(value="defaultPort") String defaultPort) throws CreatingSettingsFolderException, ReadingEcosystemException, CreatingMicroserviceScriptException, SavingEcosystemException {
		ecosystemManager.setNewMicroservice(name, pomLocation, defaultPort);
    }
	
	@RequestMapping(value= "/removemicroservice", method = RequestMethod.POST)
	@ResponseBody
    public void removeMicroservice(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException{
		ecosystemManager.removeMicroservice(id);
    }
	
	@RequestMapping(value= "/startinstance", method = RequestMethod.POST)
	@ResponseBody
    public void startInstance(@RequestParam(value="id") String id, @RequestParam(value="port") String port) throws CreatingSettingsFolderException, ReadingEcosystemException, RunningMicroserviceScriptException, SavingEcosystemException {
		ecosystemManager.startInstance(id, port);
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

	@RequestMapping(value= "/metrics", method = RequestMethod.POST)
	@ResponseBody
	public Queue<Metrics> getMetrics(@RequestParam(value="id") String id) {
		return metricsCollector.getInstanceMetrics(id);
	}
}
