package org.ernest.applications.trampoline.controller;

import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardController {

	private static final String DASHBOARD_VIEW = "dashboard";


	@RequestMapping("/")
    public String greeting(Model model) {
		return DASHBOARD_VIEW;
    }
}
