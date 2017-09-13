package org.ernest.applications.trampoline.entities;


import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MicroservicesGroup {
    private String id;
    private String name;
    private List<String> microservicesIds;
}
