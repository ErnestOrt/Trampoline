package org.ernest.applications.trampoline.exceptions;

public class RunningMicroserviceScriptException extends RuntimeException {

    public RunningMicroserviceScriptException() {}

    public RunningMicroserviceScriptException(String message) {
        super(message);
    }
}
