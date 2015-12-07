package org.ernest.applications.trampoline.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.entities.Microservice;
import org.ernest.applications.trampoline.services.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonSyntaxException;

@Controller
public class HomeController {

	private static final String HOME_VIEW = "home";
	
	@Autowired
	FileManager fileManager;

	@RequestMapping("/trampoline")
    public String greeting(Model model) throws IOException {
		Ecosystem ecosystem = fileManager.getEcosystem();
		model.addAttribute("microservices", ecosystem.getMicroservices());
		model.addAttribute("instances", ecosystem.getInstances());
		model.addAttribute("mavenLocation", ecosystem.getMavenLocation() == null ? "Please set maven Location. Ex: /Users/ernest/Documents/workspace/tools/apache-maven-3.2.1" : ecosystem.getMavenLocation());
		return HOME_VIEW;
    }
	
	@RequestMapping(value= "/setmavenlocation", method = RequestMethod.POST)
	@ResponseBody
    public void setMavenLocation(@RequestParam(value="path") String path) throws JsonSyntaxException, IOException {
		fileManager.setMavenLocation(path);
    }
	
	@RequestMapping(value= "/setnewmicroservice", method = RequestMethod.POST)
	@ResponseBody
    public void setNewMicroservice(@RequestParam(value="name") String name, @RequestParam(value="pomLocation") String pomLocation, @RequestParam(value="defaultPort") String defaultPort) throws JsonSyntaxException, IOException {
		fileManager.setNewMicroservice(name, pomLocation, defaultPort);
    }
	
	@RequestMapping(value= "/removemicroservice", method = RequestMethod.POST)
	@ResponseBody
    public void removeMicroservice(@RequestParam(value="id") String id) throws JsonSyntaxException, IOException {
		fileManager.removeMicroservice(id);
    }
	
	@RequestMapping(value= "/startinstance", method = RequestMethod.POST)
	@ResponseBody
    public void startInstance(@RequestParam(value="id") String id, @RequestParam(value="port") String port) throws JsonSyntaxException, IOException {
		fileManager.startInstance(id, port);
    }
	
	@RequestMapping(value= "/health", method = RequestMethod.POST)
	@ResponseBody
    public String checkStatusInstance(@RequestParam(value="id") String id) throws JsonSyntaxException, IOException {
		return fileManager.getStatusInstance(id);
    }
	
	@RequestMapping(value= "/killinstance", method = RequestMethod.POST)
	@ResponseBody
    public void killInstance(@RequestParam(value="id") String id) throws Exception {
		fileManager.killInstance(id);
    }
	
	@RequestMapping(value= "/removenotdeployedinstances", method = RequestMethod.POST)
	@ResponseBody
    public void removeNotDeployedInstances() throws Exception {
		fileManager.removeNotDeployedInstances();
    }
}
