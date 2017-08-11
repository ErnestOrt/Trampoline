package org.ernest.applications.trampoline.controller;

import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.exceptions.CreatingMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.ernest.applications.trampoline.exceptions.SavingEcosystemException;
import org.ernest.applications.trampoline.services.EcosystemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/settings")
public class SettingsController {

	private static final String SETTINGS_VIEW = "settings";

	@Autowired
	EcosystemManager ecosystemManager;

	@RequestMapping("/")
    public String getSettingsView(Model model) {
		Ecosystem ecosystem = ecosystemManager.getEcosystem();

		model.addAttribute("microservices", ecosystem.getMicroservices());
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
								   @RequestParam(value="vmArguments") String vmArguments) throws CreatingSettingsFolderException, ReadingEcosystemException, CreatingMicroserviceScriptException, SavingEcosystemException {
		ecosystemManager.setNewMicroservice(name, pomLocation, defaultPort, actuatorPrefix, vmArguments);
	}

	@RequestMapping(value= "/removemicroservice", method = RequestMethod.POST)
	@ResponseBody
	public void removeMicroservice(@RequestParam(value="id") String id) throws CreatingSettingsFolderException, ReadingEcosystemException, SavingEcosystemException{
		ecosystemManager.removeMicroservice(id);
	}
}
