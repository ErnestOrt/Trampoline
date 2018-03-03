package org.ernest.applications.trampoline.controller;

import org.ernest.applications.trampoline.services.TrampolineManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@RequestMapping("/trampoline")
public class TrampolineManagerController {

    @Autowired
    TrampolineManager trampolineManager;

    @RequestMapping("/shutdown")
    public String getShutdownView() {
        new Thread(() -> trampolineManager.shutdown()).start();
        return "shutdown";
    }
}
