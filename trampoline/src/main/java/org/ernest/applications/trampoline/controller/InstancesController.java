package org.ernest.applications.trampoline.controller;

import org.ernest.applications.trampoline.collectors.InstanceInfoCollector;
import org.ernest.applications.trampoline.entities.*;
import org.ernest.applications.trampoline.exceptions.*;
import org.ernest.applications.trampoline.services.EcosystemManager;
import org.ernest.applications.trampoline.collectors.MetricsCollector;
import org.ernest.applications.trampoline.collectors.TraceCollector;
import org.ernest.applications.trampoline.utils.PortsChecker;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Queue;

@Controller
@RequestMapping("/instances")
public class InstancesController {

	private static final String INSTANCES_VIEW = "instances";

	@Autowired
	EcosystemManager ecosystemManager;

	@Autowired
	MetricsCollector metricsCollector;

	@Autowired
	TraceCollector traceCollector;

	@Autowired
	InstanceInfoCollector instanceInfoCollector;

	@RequestMapping("")
    public String getInstanceView(Model model) {
		Ecosystem ecosystem = ecosystemManager.getEcosystem();
		model.addAttribute("microservices", ecosystem.getMicroservices());
		model.addAttribute("externalInstances", ecosystem.getExternalInstances());
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
	public void startInstance(@RequestParam(value="id") String id, @RequestParam(value="port") String port, @RequestParam(value="vmArguments") String vmArguments) throws CreatingSettingsFolderException, ReadingEcosystemException, RunningMicroserviceScriptException, SavingEcosystemException, InterruptedException {
		ecosystemManager.startInstance(id, port, vmArguments, 0);
	}

	@RequestMapping(value= "/restartinstance", method = RequestMethod.POST)
	@ResponseBody
	public void restartInstance(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException, ShuttingDownInstanceException, InterruptedException {
		ecosystemManager.restartInstance(id);
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
	public List<TraceActuator> getTraces(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException, JSONException {
		return traceCollector.getTraces(id);
	}

	@RequestMapping(value= "/info", method = RequestMethod.POST)
	@ResponseBody
	public InstanceGitInfo getInstanceInfoWhenDeployed(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException {
		return instanceInfoCollector.getInfo(id);
	}

	@RequestMapping(value= "/checkport", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkPort(@RequestParam(value="port") int port) throws CreatingSettingsFolderException, ReadingEcosystemException {
		boolean declaredInstanceOnPort = ecosystemManager.getEcosystem().getInstances().stream().anyMatch(i -> i.getPort().equals(String.valueOf(port)));

		return declaredInstanceOnPort == false ? PortsChecker.available(port) : false;
	}

	@RequestMapping(value= "/startgroup", method = RequestMethod.POST)
	@ResponseBody
	public void startGroup(@RequestParam(value="id") String id) throws InterruptedException {
		ecosystemManager.startGroup(id);
	}

	@RequestMapping(value= "/addexternalinstance", method = RequestMethod.POST)
	@ResponseBody
	public void addExternalInstance(@RequestParam(value="id") String id)  {
		ecosystemManager.addExternalInstance(id);
	}

}

