package com.webscraping.analyse.model;

public enum HTMLVersion {
    HTML5("HTML 5"), HTML4("HTML 4"), HTML_OLD("Below HTML 4");

    private String value;

    HTMLVersion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
