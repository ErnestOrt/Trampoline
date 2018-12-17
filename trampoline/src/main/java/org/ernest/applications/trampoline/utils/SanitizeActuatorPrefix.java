package org.ernest.applications.trampoline.utils;

public class SanitizeActuatorPrefix {
    public static String clean(String actuatorPrefix){
        return actuatorPrefix.replaceFirst("^/","");
    }
}
