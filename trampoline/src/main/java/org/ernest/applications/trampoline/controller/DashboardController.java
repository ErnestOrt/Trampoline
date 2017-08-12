package org.ernest.applications.trampoline.controller;

import org.ernest.applications.trampoline.collectors.DashboardCollector;
import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.entities.MemoryUsageDto;
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

import java.util.Queue;

@Controller
public class DashboardController {

	private static final String DASHBOARD_VIEW = "dashboard";

	@Autowired
	EcosystemManager ecosystemManager;

	@Autowired
	DashboardCollector dashboardCollector;

	@RequestMapping("/")
    public String greeting(Model model) {
		Ecosystem ecosystem = ecosystemManager.getEcosystem();

		model.addAttribute("microservicesRegistered", ecosystem.getMicroservices().size());
		model.addAttribute("instancesSize", ecosystem.getInstances().size());
		model.addAttribute("mavenHomeLocationMessage", ecosystem.getMavenHomeLocation() == null ? "No" : "Yes");
		model.addAttribute("javaVersion", System.getProperty("java.version"));



		return DASHBOARD_VIEW;
    }

	@RequestMapping(value= "/dashboard/memory", method = RequestMethod.GET)
	@ResponseBody
	public Queue<MemoryUsageDto> checkStatusInstance()  {
		return dashboardCollector.getMemoryUsageQueue();
	}
}
