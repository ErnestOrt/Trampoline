package org.ernest.applications.trampoline.entities;


public class MemoryUsageDto {

    private String date;
    private String freeMemoryMB;
    private String totalMemoryMB;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFreeMemoryMB() {
        return freeMemoryMB;
    }

    public void setFreeMemoryMB(String freeMemoryMB) {
        this.freeMemoryMB = freeMemoryMB;
    }

    public String getTotalMemoryMB() {
        return totalMemoryMB;
    }

    public void setTotalMemoryMB(String totalMemoryMB) {
        this.totalMemoryMB = totalMemoryMB;
    }
}
