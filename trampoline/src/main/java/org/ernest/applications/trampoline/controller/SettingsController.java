package org.ernest.applications.trampoline.controller;

import org.ernest.applications.trampoline.entities.*;
import org.ernest.applications.trampoline.exceptions.*;
import org.ernest.applications.trampoline.services.EcosystemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/settings")
public class SettingsController {

	private static final String SETTINGS_VIEW = "settings";
	private final EcosystemManager ecosystemManager;

    @Autowired
    public SettingsController(EcosystemManager ecosystemManager) {
        this.ecosystemManager = ecosystemManager;
    }

    @RequestMapping
    public String getSettingsView(Model model) {
		Ecosystem ecosystem = ecosystemManager.getEcosystem();

		model.addAttribute("microservices", ecosystem.getMicroservices());
		model.addAttribute("microservicesgroups", ecosystem.getMicroservicesGroups());
		model.addAttribute("mavenHomeLocation", ecosystem.getMavenHomeLocation());
		model.addAttribute("mavenBinaryLocationMessage", ecosystem.getMavenBinaryLocation() == null ? "Set Maven Binary Location if necessary. Otherwise it will automatically be searched in a bin folder inside your Maven Home Location" : ecosystem.getMavenBinaryLocation());
		model.addAttribute("mavenHomeLocationMessage", ecosystem.getMavenHomeLocation() == null ? "Please set maven Home Location. Ex: /Users/ernest/Documents/workspace/tools/apache-maven-3.2.1" : ecosystem.getMavenHomeLocation());

		return SETTINGS_VIEW;
    }

	@RequestMapping(value= "/setmaven", method = RequestMethod.POST)
	@ResponseBody
	public void setMavenBinaryLocation(@RequestParam(value="mavenHomeLocationPath") String mavenHomeLocationPath,@RequestParam(value="mavenBinaryLocationPath") String mavenBinaryLocationPath) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException {
		ecosystemManager.setMavenBinaryLocation(mavenBinaryLocationPath);
		ecosystemManager.setMavenHomeLocation(mavenHomeLocationPath);
	}

	@RequestMapping(value= "/setnewmicroservice", method = RequestMethod.POST)
	@ResponseBody
	public void setNewMicroservice(@RequestParam(value="name") String name, @RequestParam(value="pomLocation") String pomLocation,
								   @RequestParam(value="defaultPort") String defaultPort, @RequestParam(value="actuatorPrefix") String actuatorPrefix,
								   @RequestParam(value="vmArguments") String vmArguments, @RequestParam(value="buildTool") String buildTool) throws CreatingSettingsFolderException, ReadingEcosystemException, CreatingMicroserviceScriptException, SavingEcosystemException {
		ecosystemManager.setNewMicroservice(name, pomLocation, defaultPort, actuatorPrefix, vmArguments, buildTool);
	}

	@RequestMapping(value= "/removemicroservice", method = RequestMethod.POST)
	@ResponseBody
	public void removeMicroservice(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException{
		ecosystemManager.removeMicroservice(id);
	}

	@RequestMapping(value= "/microserviceinfo", method = RequestMethod.POST)
	@ResponseBody
	public Microservice getMicroserviceInfo(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException, CreatingMicroserviceScriptException, SavingEcosystemException {
		return ecosystemManager.getEcosystem().getMicroservices().stream().filter(m-> m.getId().equals(id)).findFirst().get();
	}

	@RequestMapping(value= "/setmicroservicesgroup", method = RequestMethod.POST)
	@ResponseBody
	public void getMicroserviceInfo(@RequestParam(value="name") String name, @RequestParam(value="idsMicroservicesGroup[]") List<String> idsMicroservicesGroup) throws CreatingSettingsFolderException, ReadingEcosystemException, CreatingMicroserviceScriptException, SavingEcosystemException {
		ecosystemManager.setMicroserviceGroup(name, idsMicroservicesGroup);
	}

	@RequestMapping(value= "/groupinfo", method = RequestMethod.POST)
	@ResponseBody
	public MicroserviceGroupInfo getGroupInfo(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException, CreatingMicroserviceScriptException, SavingEcosystemException {
		MicroservicesGroup microservicesGroup = ecosystemManager.getEcosystem().getMicroservicesGroups().stream().filter(m-> m.getId().equals(id)).findFirst().get();
		List<Microservice> microservices = ecosystemManager.getEcosystem().getMicroservices();

		MicroserviceGroupInfo info = new MicroserviceGroupInfo();
		info.setName(microservicesGroup.getName());
		info.setMicroservicesNames(microservices.stream()
				                                .filter(m->microservicesGroup.getMicroservicesIds().contains(m.getId()))
				                                .map(Microservice::getName)
				                                .collect(Collectors.toList()));
		return info;
	}

	@RequestMapping(value= "/removegroup", method = RequestMethod.POST)
	@ResponseBody
	public void removeGroup(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException{
		ecosystemManager.removeGroup(id);
	}

    @RequestMapping(value = "/load", method = RequestMethod.POST)
    @ResponseBody
    public MicroserviceConfiguration loadSettings(@RequestParam(value = "location") String location) {
        Optional<MicroserviceConfiguration> configuration = ecosystemManager.loadConfigurations(location);
        return configuration.orElseThrow(ConfigurationFileNotFoundExcpetion::new);
    }
}