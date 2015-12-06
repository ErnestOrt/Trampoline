package org.ernest.applications.trampoline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewsController {

	@RequestMapping("/trampoline")
    public String greeting(Model model) {
        return "index";
    }
}
