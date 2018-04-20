package org.ernest.applications.trampoline.entities;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MicroservicesGroup {

    private String id;
    private String name;
    private List<String> microservicesIds;
}
