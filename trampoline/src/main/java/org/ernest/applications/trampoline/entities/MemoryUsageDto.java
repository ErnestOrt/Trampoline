package org.ernest.applications.trampoline.entities;

import lombok.Data;

@Data
public class MemoryUsageDto {

    private String date;
    private String freeMemoryMB;
    private String totalMemoryMB;
}
