package org.ernest.applications.trampoline.entities;

import lombok.Data;

@Data
public class Metrics {

    private String date;
    private Long totalMemoryKB;
    private Long freeMemoryKB;
    private Long heapKB;
    private Long initHeapKB;
    private Long usedHeapKB;
}