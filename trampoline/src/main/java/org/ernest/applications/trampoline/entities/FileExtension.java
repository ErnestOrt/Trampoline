package org.ernest.applications.trampoline.entities;

public enum FileExtension {
    PROPERTIES("properties"),
    YML("yml");

    private String value;

    FileExtension(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
