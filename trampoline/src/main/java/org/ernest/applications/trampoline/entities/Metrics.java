package org.ernest.applications.trampoline.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Metrics {

    private String date;
    private Long totalMemoryKB;
    private Long freeMemoryKB;
    private Long heapKB;
    private Long initHeapKB;
    private Long usedHeapKB;
}