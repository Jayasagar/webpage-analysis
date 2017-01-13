package com.webscraping.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public final class URIUtils {

    private URIUtils() {}

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

    public static boolean isAbsoluteUrl(String urlValue) {
        URI uri = getUri(urlValue);

        return uri.isAbsolute();
    }

    public static String getDomainName(String uriValue) {
        URI uri = getUri(uriValue);

        String domain = uri.getHost();

        // returns "" value if domain is null
        if (Objects.isNull(domain)) {
            return "";
        }
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public static URI getUri(String uriValue) {
        URI uri = null;
        try {
            uri = new URI(uriValue);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }

        Objects.requireNonNull(uri);
        return uri;
    }
}
