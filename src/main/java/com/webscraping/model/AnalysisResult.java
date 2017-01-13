package com.webscraping.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

/**
 * Model to hold the complete page analysis result.
 */
@Getter @Setter
@ToString
@Builder
public class AnalysisResult {
    private String htmlVersion;
    private String pageTitle;
    private Map<String, Long> headings;
    private Map<LinkGroup, Set<HypermediaLink>> hypermediaLinks;
    private boolean loginForm;

}
