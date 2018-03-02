package org.ernest.applications.trampoline.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class TrampolineManager {

    private final Logger log = LoggerFactory.getLogger(TrampolineManager.class);

    @Autowired
    private ApplicationContext appContext;

    public void shutdown(){
        log.info("Shutdowning Trampoline... #HappyCoding");
        try {Thread.sleep(2000);} catch (InterruptedException e) {throw new RuntimeException();}

        SpringApplication.exit(appContext, () -> 0);
    }

}
