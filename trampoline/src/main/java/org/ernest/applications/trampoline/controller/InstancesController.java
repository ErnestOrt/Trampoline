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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Queue;

import lombok.AllArgsConstructor;


@RestController
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
	public String checkStatusInstance(@RequestParam(value="id") String id) {
		return ecosystemManager.getStatusInstance(id);
	}

	@RequestMapping(value= "/instanceinfo", method = RequestMethod.POST)
	public Microservice getInstanceInfo(@RequestParam(value="id") String id){
		return ecosystemManager.getEcosystem().getMicroservices().stream().filter(m-> m.getId().equals(id)).findFirst().get();
	}

	@RequestMapping(value= "/startinstance", method = RequestMethod.POST)
	public void startInstance(@RequestParam(value="id") String id, @RequestParam(value="port") String port, @RequestParam(value="vmArguments") String vmArguments){
		ecosystemManager.startInstance(id, port, vmArguments);
	}

	@RequestMapping(value= "/killinstance", method = RequestMethod.POST)
	public void killInstance(@RequestParam(value="id") String id) {
		ecosystemManager.killInstance(id);
	}

	@RequestMapping(value= "/metrics", method = RequestMethod.POST)
	public Queue<Metrics> getMetrics(@RequestParam(value="id") String id) {
		return metricsCollector.getInstanceMetrics(id);
	}

	@RequestMapping(value= "/traces", method = RequestMethod.POST)
	public List<TraceActuator> getTraces(@RequestParam(value="id") String id){
		return traceCollector.getTraces(id);
	}

	@RequestMapping(value= "/checkport", method = RequestMethod.POST)
	public boolean checkPort(@RequestParam(value="port") int port) {
		boolean declaredInstanceOnPort = ecosystemManager.getEcosystem().getInstances().stream().anyMatch(i -> i.getPort().equals(String.valueOf(port)));

		return !declaredInstanceOnPort && PortsChecker.available(port);
	}

	@RequestMapping(value= "/startgroup", method = RequestMethod.POST)
	public void startGroup(@RequestParam(value="id") String id)  {
		ecosystemManager.startGroup(id);
	}
}

