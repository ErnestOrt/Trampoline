package org.ernest.applications.trampoline.entities;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MemoryUsageDto {
    private String date;
    private String freeMemoryMB;
    private String totalMemoryMB;
}
