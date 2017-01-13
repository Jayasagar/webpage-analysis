package com.webscraping.util;

import com.webscraping.model.HypermediaLink;
import com.webscraping.model.LinkGroup;

import java.util.function.Function;

public class ElementToLinkGroup implements Function<HypermediaLink, LinkGroup> {
    private String submittedUrl;

    ElementToLinkGroup(String submittedUrl) {
        this.submittedUrl = submittedUrl;
    }

    @Override
    public LinkGroup apply(HypermediaLink hypermediaLink) {
        String url = hypermediaLink.getLink();

        // If it is absolute url and not a same domain then it is external
        if (URIUtils.isAbsoluteUrl(url) && !URIUtils.isSameDomain(submittedUrl, url)) {
            return LinkGroup.EXTERNAL;
        } else {
            return LinkGroup.INTERNAL;
        }
    }
}