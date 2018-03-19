package org.ernest.applications.trampoline.entities;


public class TraceActuator {

    private String date;
    private String method;
    private String path;
    private String status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TraceActuator{" +
                "date='" + date + '\'' +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
