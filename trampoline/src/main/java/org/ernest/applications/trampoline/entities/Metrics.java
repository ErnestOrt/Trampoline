package org.ernest.applications.trampoline.entities;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Metrics {
    private String date;
    private Long totalMemoryKB;
    private Long freeMemoryKB;
    private Long heapKB;
    private Long initHeapKB;
    private Long usedHeapKB;
}