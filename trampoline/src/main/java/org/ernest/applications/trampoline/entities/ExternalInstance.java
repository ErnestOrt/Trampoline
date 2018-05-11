package org.ernest.applications.trampoline.entities;

import lombok.Data;

@Data
public class ExternalInstance {

    private String id;
    private String ip;
    private String name;
    private String port;
    private String actuatorPrefix;
}
