package com.webscraping;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

/**
 * URI extension helper utility methods to handle specific url needs.
 */
@Slf4j
public final class URIUtils {

    private URIUtils() {}

    /**
     * Validates whether given two urls are of same domain or not.
     * @param sourceUrl User submitted url in web form.
     * @param scannedUrl url found through the web scraping analysis.
     * @return Returns true if scannedUrl is equals to sourceUrl or sub domain of sourceUrl.
     */
    public static boolean isSameDomain(String sourceUrl, String scannedUrl) {
        String sourceDomainName = getDomainName(sourceUrl);
        String scannedDomainName = getDomainName(scannedUrl);

        // Both are equal
        if (sourceDomainName.equals(scannedDomainName)) {
            return true;
        }

        // check is it sub domain?
        if (scannedDomainName.endsWith(sourceDomainName)) {
            return true;
        }

        return false;
    }

    /**
     * Returns true if the given url is absolute urls.
     * This method returns false if there is a exception during the {@link URI} construction.
     * Example: return false if given input is mailto:abc@gmail.com as it throws the URI Syntax exception.
     */
    public static boolean isAbsoluteUrl(String urlValue) {
        URI uri = null;
        try {
            uri = getUri(urlValue);
        } catch (MalformedURLException | URISyntaxException e) {
            log.warn(String.format("Problem with URI creation for given url %s ", urlValue));
            return false;
        }
        return uri.isAbsolute();
    }

    /**
     * First converts the given url into {@link URI} and then returns the domain name from URI object.
     * @return Returns domain name, however it may return empty string if {@link URI} unable to construct.
     */
    public static String getDomainName(String urlValue) {
        URI uri = null;
        try {
            uri = getUri(urlValue);
        } catch (MalformedURLException | URISyntaxException e) {
            log.warn(String.format("Problem with URI creation for given url %s ", urlValue));
            return "";
        }

        Objects.requireNonNull(uri);
        String domain = uri.getHost();

        // returns "" value if domain is null
        if (Objects.isNull(domain)) {
            return "";
        }
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public static URI getUri(String uriValue) throws MalformedURLException, URISyntaxException {
        URL url = new URL(uriValue);
        return new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
    }
}
