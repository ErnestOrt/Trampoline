package org.ernest.applications.trampoline.entities;

import lombok.Data;

import java.util.List;

@Data
public class MicroserviceGroupInfo {
    private String name;
    private List<String> microservicesNames;
}
