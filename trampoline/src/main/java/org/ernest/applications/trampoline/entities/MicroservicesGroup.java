package org.ernest.applications.trampoline.entities;

import lombok.Data;

import java.util.List;

@Data
public class MicroservicesGroup {

    private String id;
    private String name;
    private List<String> microservicesIds;
}
