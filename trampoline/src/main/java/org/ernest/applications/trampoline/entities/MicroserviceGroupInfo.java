package org.ernest.applications.trampoline.entities;

import java.util.List;


public class MicroserviceGroupInfo {
    private String name;
    private List<String> microservicesNames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMicroservicesNames() {
        return microservicesNames;
    }

    public void setMicroservicesNames(List<String> microservicesNames) {
        this.microservicesNames = microservicesNames;
    }
}
