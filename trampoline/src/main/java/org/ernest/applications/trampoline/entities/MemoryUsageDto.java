package org.ernest.applications.trampoline.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemoryUsageDto {

    private String date;
    private String freeMemoryMB;
    private String totalMemoryMB;
}
