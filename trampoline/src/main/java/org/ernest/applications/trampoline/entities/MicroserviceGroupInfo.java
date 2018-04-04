package org.ernest.applications.trampoline.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MicroserviceGroupInfo {
    private String name;
    private List<String> microservicesNames;
}
