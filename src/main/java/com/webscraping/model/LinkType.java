package com.webscraping.model;

public enum LinkType {
    MEDIA("Media"), LINK("link"), IMPORT("Import link");

    private String value;

    LinkType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
