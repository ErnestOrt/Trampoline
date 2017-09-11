package org.ernest.applications.trampoline.entities;


import java.util.List;

public class MicroservicesGroup {

    private String id;
    private String name;
    private List<String> microservicesIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMicroservicesIds() {
        return microservicesIds;
    }

    public void setMicroservicesIds(List<String> microservicesIds) {
        this.microservicesIds = microservicesIds;
    }


}
