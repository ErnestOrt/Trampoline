package org.ernest.applications.trampoline.entities;


import java.util.Date;

public class Metrics {

    private String date;
    private Long totalMemoryKB;
    private Long freeMemoryKB;
    private Long heapKB;
    private Long initHeapKB;
    private Long usedHeapKB;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTotalMemoryKB() {
        return totalMemoryKB;
    }

    public void setTotalMemoryKB(Long totalMemoryKB) {
        this.totalMemoryKB = totalMemoryKB;
    }

    public Long getFreeMemoryKB() {
        return freeMemoryKB;
    }

    public void setFreeMemoryKB(Long freeMemoryKB) {
        this.freeMemoryKB = freeMemoryKB;
    }

    public Long getHeapKB() {
        return heapKB;
    }

    public void setHeapKB(Long heapKB) {
        this.heapKB = heapKB;
    }

    public Long getInitHeapKB() {
        return initHeapKB;
    }

    public void setInitHeapKB(Long initHeapKB) {
        this.initHeapKB = initHeapKB;
    }

    public Long getUsedHeapKB() {
        return usedHeapKB;
    }

    public void setUsedHeapKB(Long usedHeapKB) {
        this.usedHeapKB = usedHeapKB;
    }

    @Override
    public String toString() {
        return "Metrics{" +
                "date=" + date +
                ", totalMemoryKB=" + totalMemoryKB +
                ", freeMemoryKB=" + freeMemoryKB +
                ", heapKB=" + heapKB +
                ", initHeapKB=" + initHeapKB +
                ", usedHeapKB=" + usedHeapKB +
                '}';
    }
}