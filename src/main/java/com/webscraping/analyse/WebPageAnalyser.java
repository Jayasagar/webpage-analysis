package com.webscraping.analyse;

import com.webscraping.ErrorMessages;
import com.webscraping.analyse.model.*;
import com.webscraping.model.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

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
        // Basic url validation
        Connection connection = null;
        try {
            connection = Jsoup
                            .connect(url)
                            .timeout(5000);
        } catch (IllegalArgumentException iae) {
            log.error(String.format("Unable to analyse the url %s. Check the url.", url));
            return ResponseEntity.error(ErrorMessages.INVALID_URL_FORMAT);
        }

        Objects.requireNonNull(connection);

        // Connection timeout validation
        Document document = null;
        try {
            document = connection.get();
        } catch (IllegalArgumentException e) {
            log.error(String.format("It is not a valid http url. Check the url ", url));
            return ResponseEntity.error(ErrorMessages.INVALID_HTTP_URL);
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

    /**
     * This method only checks for the input element of type password. If presents then returns true else false.
     */
    boolean isLoginForm(Document document) {
        return !document.select("input[type=password]").isEmpty();
    }

    /**
     * Returns the Map of all the html heading levels and their count.
     */
    Map<String, Long> getHeadingGroupByLevel(Document document) {
        // Select all the heading level elements
        Elements headingTags = document.select("h1, h2, h3, h4, h5, h6");

        return headingTags
                .stream()
                // Group by heading level and their total count
                .collect(groupingBy(element -> element.tagName(), counting()));
    }

    /**
     *  Returns the HTML page version.
     *  If HTML page has only <!DOCTYPE HTML></!DOCTYPE> then identifies as HTML 5 and returns the same.
     *  If HTML page has only <!DOCTYPE HTML> with path to dtd, then returns the HTML 4.
     *  Returns empty string if there are no child nodes in the given {@link Document}
     */
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
        Set<HypermediaLink> anchorTagList = collectHypermediaLinks(links, LinkType.LINK);

        // Transfer all import tags to set of HypermediaLink objects
        Set<HypermediaLink> importList = collectHypermediaLinks(imports, LinkType.IMPORT);

        // Transfer all media tags to set of HypermediaLink objects
        Set<HypermediaLink> mediaList = collectHypermediaLinks(media, LinkType.MEDIA);

        // combine all type of links
        Set<HypermediaLink> allLinks = new HashSet<>(anchorTagList);
        allLinks.addAll(importList);
        allLinks.addAll(mediaList);

        // Group all links by internal and external
        Map<LinkGroup, Set<HypermediaLink>> groupByDomain = allLinks.stream()
                .collect(groupingBy(new ElementToLinkGroup(submittedUrl), Collectors.toSet()));

        return groupByDomain;
    }

    String getPageTitle(Document document) {
        return document.title();
    }

    private Set<HypermediaLink> collectHypermediaLinks(Elements links, LinkType linkType) {
        return links
                    .stream()
                    .map(new ElementToLink(linkType))
                    .collect(Collectors.toSet());
    }
}
