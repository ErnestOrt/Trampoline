package org.ernest.applications.trampoline.controller;

import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.entities.Microservice;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.ernest.applications.trampoline.services.EcosystemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InstancesController {

	private static final String INSTANCES_VIEW = "instances";

	@Autowired
	EcosystemManager ecosystemManager;


	@RequestMapping("/instances")
    public String getInstanceView(Model model) {
		Ecosystem ecosystem = ecosystemManager.getEcosystem();
		model.addAttribute("microservices", ecosystem.getMicroservices());
		model.addAttribute("instances", ecosystem.getInstances());

		return INSTANCES_VIEW;
    }

	@RequestMapping(value= "/instances/health", method = RequestMethod.POST)
	@ResponseBody
	public String checkStatusInstance(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException {
		return ecosystemManager.getStatusInstance(id);
	}

	@RequestMapping(value= "//instances/instanceinfo", method = RequestMethod.POST)
	@ResponseBody
	public Microservice getInstanceInfo(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException {
		return ecosystemManager.getEcosystem().getMicroservices().stream().filter(m-> m.getId().equals(id)).findFirst().get();
	}
}
