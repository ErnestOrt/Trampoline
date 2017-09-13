package org.ernest.applications.trampoline.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MicroserviceConfiguration {
    private Map<String, Object> server;

    public Map<String, Object> getServer() {
        return server;
    }

    public MicroserviceConfiguration setServer(Map<String, Object> server) {
        this.server = server;
        return this;
    }
}
