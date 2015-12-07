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
	
	private static final String WORKING_DIRECTORY_PATH_ATRIBUTTE = "WORKING_DIRECTORY_PATH";
	
	private static final String WORKING_DIRECTORY_MESSAGE = "Your working directory is: ";
	private static final String NOT_WORKING_DIRECTORY_MESSAGE = "Ups... you need to set up the working directory";
	
	@Autowired
	FileManager fileManager;

	@RequestMapping("/trampoline")
    public String greeting(Model model, HttpServletRequest request) throws IOException {
		String workingDirectory = ((String)request.getSession().getAttribute(WORKING_DIRECTORY_PATH_ATRIBUTTE));
		
		model.addAttribute("workingDirectory", workingDirectory == null ? NOT_WORKING_DIRECTORY_MESSAGE : WORKING_DIRECTORY_MESSAGE + workingDirectory);
        
		if(workingDirectory!=null){
			Ecosystem ecosystem = fileManager.getEcosystem((String)request.getSession().getAttribute(WORKING_DIRECTORY_PATH_ATRIBUTTE));
			model.addAttribute("microservices", ecosystem.getMicroservices());
		}
		
		return HOME_VIEW;
    }
	
	@RequestMapping(value= "/setworkingdirectory", method = RequestMethod.POST)
	@ResponseBody
    public void setWorkingDirectory(@RequestParam(value="path") String path, HttpServletRequest request) {
		request.getSession().setAttribute(WORKING_DIRECTORY_PATH_ATRIBUTTE, path);
    }
	
	@RequestMapping(value= "/setnewmicroservice", method = RequestMethod.POST)
	@ResponseBody
    public void setNewMicroservice(@RequestParam(value="name") String name, @RequestParam(value="pomLocation") String pomLocation, @RequestParam(value="defaultPort") String defaultPort, HttpServletRequest request) throws JsonSyntaxException, IOException {
		Microservice microservice = new Microservice();
		microservice.setId(UUID.randomUUID().toString());
		microservice.setName(name);
		microservice.setPomLocation(pomLocation);
		microservice.setDefaultPort(defaultPort);
		
		fileManager.setNewMicroservice((String)request.getSession().getAttribute(WORKING_DIRECTORY_PATH_ATRIBUTTE), microservice);
    }
}
