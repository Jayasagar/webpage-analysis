package com.webscraping.analyse;

import com.webscraping.analyse.model.HypermediaLink;
import com.webscraping.analyse.model.LinkGroup;
import com.webscraping.URIUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.function.Function;

/**
 * Responsible for returning the {@link LinkGroup} based on the {@link HypermediaLink}.
 */
@Slf4j
public class ElementToLinkGroup implements Function<HypermediaLink, LinkGroup> {
    private String submittedUrl;

    ElementToLinkGroup(String submittedUrl) {
        this.submittedUrl = submittedUrl;
    }

    @Override
    public LinkGroup apply(HypermediaLink hypermediaLink) {
        String url = hypermediaLink.getLink();

        // If it is absolute url and not a same domain as submitted url then it is external domain.
        if (URIUtils.isAbsoluteUrl(url) && !URIUtils.isSameDomain(submittedUrl, url)) {
            return LinkGroup.EXTERNAL;
        } else {
            return LinkGroup.INTERNAL;
        }
    }
}