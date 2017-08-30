package org.ernest.applications.trampoline.entities;

import java.util.Arrays;

public enum BuildTools {
    MAVEN("maven"),
    GRADLE("gradle");

    private String code;

    BuildTools(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static BuildTools getByCode(String code) {
        return Arrays.asList(BuildTools.values()).stream().filter(p -> code.equals(p.getCode())).findFirst().get();
    }
}
