package org.ernest.applications.trampoline.entities;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BuildTools {
    MAVEN("maven"),
    GRADLE("gradle");

    private String code;

    BuildTools(String code){
        this.code = code;
    }

    public static BuildTools getByCode(String code) {
        return Arrays.stream(BuildTools.values()).filter(p -> code.equals(p.getCode())).findFirst().get();
    }
}
