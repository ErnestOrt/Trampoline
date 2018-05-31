package org.ernest.applications.trampoline.entities;

public class ExternalInstance {

    private String id;
    private String ip;
    private String name;
    private String port;
    private String actuatorPrefix;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getActuatorPrefix() {
        return actuatorPrefix;
    }

    public void setActuatorPrefix(String actuatorPrefix) {
        this.actuatorPrefix = actuatorPrefix;
    }

    @Override
    public String toString() {
        return "ExternalInstance{" +
                "id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                ", port='" + port + '\'' +
                ", actuatorPrefix='" + actuatorPrefix + '\'' +
                '}';
    }
}
