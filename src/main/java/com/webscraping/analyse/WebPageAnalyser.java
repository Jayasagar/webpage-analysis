package com.webscraping.analyse;

import com.webscraping.analyse.model.*;
import com.webscraping.model.*;
import com.webscraping.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * Responsible for analysing the given url.
 */
@Component
@Slf4j
public class WebPageAnalyser {

    /**
     * Using Jsoup, this method parse the given url, extracts the data and aggregate into {@link AnalysisResult}.
     * @param url user submitted url to analyse.
     */
    public ResponseEntity<AnalysisResult> analysePage(String url) {
        Connection connection = null;
        try {
            connection = Jsoup
                            .connect(url)
                            .timeout(5000);
        } catch (IllegalArgumentException iae) {
            log.error(String.format("Unable to analyse the url %s. Check the url.", url));
            return ResponseEntity.error(ErrorMessages.INVALID_URL);
        }

        Objects.requireNonNull(connection);

        Document document = null;
        try {
            document = connection.get();
        } catch (IOException e) {
            log.error(String.format("Unable to analyse the url %s. Timeout exception. ", url));
            return ResponseEntity.error(ErrorMessages.TIMEOUT_ERROR);
        }

        Objects.requireNonNull(document);

        // Get html version
        String version = getPageVersion(document);

        // Get the title
        String title = getPageTitle(document);

        // Get Hypermedia links by internal/external grouping
        Map<LinkGroup, Set<HypermediaLink>> links = getHypermediaLinks(url, document);

        // Get number of headings by level grouping
        Map<String, Long> headingCountGroupByLevel = getHeadingGroupByLevel(document);

        // Check is the given url is login page?
        boolean isLoginForm = isLoginForm(document);

        AnalysisResult analysisResult = AnalysisResult.builder()
                .htmlVersion(version)
                .pageTitle(title)
                .hypermediaLinks(links)
                .headings(headingCountGroupByLevel)
                .loginForm(isLoginForm)
                .build();

        return ResponseEntity.success(analysisResult);
    }

    boolean isLoginForm(Document document) {
        return !document.select("input[type=password]").isEmpty();
    }

    Map<String, Long> getHeadingGroupByLevel(Document document) {
        Elements headingTags = document.select("h1, h2, h3, h4, h5, h6");

        return headingTags
                .stream()
                .collect(groupingBy(element -> element.tagName(), counting()));
    }

    String getPageTitle(Document document) {
        return document.title();
    }

    String getPageVersion(Document document) {
        List<Node> nods = document.childNodes();
        for (Node node : nods) {
            if (node instanceof DocumentType) {
                //TODO: Implement to check for other html versions
                DocumentType documentType = (DocumentType)node;
                if (documentType.attr("publicId").isEmpty() && documentType.attr("systemId").isEmpty()) {
                    return HTMLVersion.HTML5.getValue();
                }

                return HTMLVersion.HTML4.getValue();
            }
        }
        return "";
    }

    Map<LinkGroup, Set<HypermediaLink>> getHypermediaLinks(String submittedUrl, Document document) {

        Elements links = document.select("a[href]");
        Elements media = document.select("[src]");
        Elements imports = document.select("link[href]");

        // Transfer all anchor tags to set of HypermediaLink objects
        Set<HypermediaLink> anchorTagList = links
                .stream()
                .map(new ElementToLink(LinkType.LINK))
                .collect(Collectors.toSet());

        log.debug("anchorTagList" + anchorTagList);

        // Transfer all import tags to set of HypermediaLink objects
        Set<HypermediaLink> importList = imports
                .stream()
                .map(new ElementToLink(LinkType.IMPORT))
                .collect(Collectors.toSet());
        log.debug("importList" + importList);

        // Transfer all media tags to set of HypermediaLink objects
        Set<HypermediaLink> mediaList = media
                .stream()
                .map(new ElementToLink(LinkType.MEDIA))
                .collect(Collectors.toSet());

        log.debug("mediaList" + mediaList);

        // combine all type of links
        Set<HypermediaLink> allLinks = new HashSet<>(anchorTagList);
        allLinks.addAll(importList);
        allLinks.addAll(mediaList);

        // Group all links by internal and external
        Map<LinkGroup, Set<HypermediaLink>> groupByDomain = allLinks.stream()
                .collect(groupingBy(new ElementToLinkGroup(submittedUrl), Collectors.toSet()));

        return groupByDomain;
    }

}
