package org.ernest.applications.trampoline.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TraceActuator {

    private String date;
    private String method;
    private String path;
    private String status;
}
