package org.ernest.applications.trampoline.controller;

import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.entities.Metrics;
import org.ernest.applications.trampoline.entities.Microservice;
import org.ernest.applications.trampoline.entities.TraceActuator;
import org.ernest.applications.trampoline.exceptions.*;
import org.ernest.applications.trampoline.services.EcosystemManager;
import org.ernest.applications.trampoline.collectors.MetricsCollector;
import org.ernest.applications.trampoline.collectors.TraceCollector;
import org.ernest.applications.trampoline.utils.PortsChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Queue;

import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
@RequestMapping("/instances")
public class InstancesController {

	private static final String INSTANCES_VIEW = "instances";

	private final EcosystemManager ecosystemManager;
	private final MetricsCollector metricsCollector;
	private final TraceCollector traceCollector;

	@RequestMapping("")
    public String getInstanceView(Model model) {
		Ecosystem ecosystem = ecosystemManager.getEcosystem();
		model.addAttribute("microservices", ecosystem.getMicroservices());
		model.addAttribute("instances", ecosystem.getInstances());
		model.addAttribute("microservicesgroups", ecosystem.getMicroservicesGroups());

		return INSTANCES_VIEW;
    }

	@RequestMapping(value= "/health", method = RequestMethod.POST)
	@ResponseBody
	public String checkStatusInstance(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException {
		return ecosystemManager.getStatusInstance(id);
	}

	@RequestMapping(value= "/instanceinfo", method = RequestMethod.POST)
	@ResponseBody
	public Microservice getInstanceInfo(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException {
		return ecosystemManager.getEcosystem().getMicroservices().stream().filter(m-> m.getId().equals(id)).findFirst().get();
	}

	@RequestMapping(value= "/startinstance", method = RequestMethod.POST)
	@ResponseBody
	public void startInstance(@RequestParam(value="id") String id, @RequestParam(value="port") String port, @RequestParam(value="vmArguments") String vmArguments) throws CreatingSettingsFolderException, ReadingEcosystemException, RunningMicroserviceScriptException, SavingEcosystemException {
		ecosystemManager.startInstance(id, port, vmArguments);
	}

	@RequestMapping(value= "/killinstance", method = RequestMethod.POST)
	@ResponseBody
	public void killInstance(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException, ShuttingDownInstanceException {
		ecosystemManager.killInstance(id);
	}

	@RequestMapping(value= "/metrics", method = RequestMethod.POST)
	@ResponseBody
	public Queue<Metrics> getMetrics(@RequestParam(value="id") String id) {
		return metricsCollector.getInstanceMetrics(id);
	}

	@RequestMapping(value= "/traces", method = RequestMethod.POST)
	@ResponseBody
	public List<TraceActuator> getTraces(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException {
		return traceCollector.getTraces(id);
	}

	@RequestMapping(value= "/checkport", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkPort(@RequestParam(value="port") int port) throws CreatingSettingsFolderException, ReadingEcosystemException {
		boolean declaredInstanceOnPort = ecosystemManager.getEcosystem().getInstances().stream().anyMatch(i -> i.getPort().equals(String.valueOf(port)));

		return !declaredInstanceOnPort && PortsChecker.available(port);
	}

	@RequestMapping(value= "/startgroup", method = RequestMethod.POST)
	@ResponseBody
	public void startGroup(@RequestParam(value="id") String id)  {
		ecosystemManager.startGroup(id);
	}
}

