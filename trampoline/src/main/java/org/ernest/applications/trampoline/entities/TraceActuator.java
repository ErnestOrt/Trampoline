package org.ernest.applications.trampoline.entities;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class TraceActuator {
    private String date;
    private String method;
    private String path;
    private String status;
}
