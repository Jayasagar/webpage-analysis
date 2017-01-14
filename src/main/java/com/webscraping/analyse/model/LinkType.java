package com.webscraping.analyse.model;

public enum LinkType {
    MEDIA("Media"), LINK("Link"), IMPORT("Import");

    private String value;

    LinkType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
