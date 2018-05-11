package org.ernest.applications.trampoline.entities;

import lombok.Data;

@Data
public class TraceActuator {

    private String date;
    private String method;
    private String path;
    private String status;
}
