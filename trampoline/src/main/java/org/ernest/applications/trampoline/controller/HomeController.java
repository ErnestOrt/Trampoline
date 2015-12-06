package org.ernest.applications.trampoline.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	private static final String WORKING_DIRECTORY_PATH = "WORKING_DIRECTORY_PATH";

	@RequestMapping("/trampoline")
    public String greeting(Model model, HttpServletRequest request) {
		String workingDirectory = ((String)request.getSession().getAttribute(WORKING_DIRECTORY_PATH));
		model.addAttribute("workingDirectory", workingDirectory == null ? "Ups... you need to set up the working directory" : "Your working directory is: " + workingDirectory);
        
		return "home";
    }
	
	@RequestMapping(value= "/setworkingdirectory", method = RequestMethod.POST)
	@ResponseBody
    public void setWorkingDirectory(@RequestParam(value="path") String path, HttpServletRequest request) {
		request.getSession().setAttribute(WORKING_DIRECTORY_PATH, path);
    }
}
