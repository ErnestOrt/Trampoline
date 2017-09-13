package org.ernest.applications.trampoline.entities;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MicroserviceGroupInfo {
    private String name;
    private List<String> microservicesNames;
}
