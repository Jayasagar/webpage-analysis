package com.webscraping.analyse;

import com.webscraping.analyse.model.HypermediaLink;
import com.webscraping.analyse.model.LinkType;
import org.jsoup.nodes.Element;

import java.util.function.Function;

public class ElementToLink implements Function<Element, HypermediaLink> {
    private LinkType type;

    ElementToLink(LinkType type) {
        this.type = type;
    }

    @Override
    public HypermediaLink apply(Element element) {
        HypermediaLink hypermediaLink = new HypermediaLink();
        switch (type) {
            case LINK:
                hypermediaLink.setLink(element.attr("abs:href"));
                hypermediaLink.setText(element.text().trim());
                break;
            case IMPORT:
                hypermediaLink.setLink(element.attr("abs:href"));
                break;
            case MEDIA:
                hypermediaLink.setLink(element.attr("abs:src"));
                break;
        }

        hypermediaLink.setType(type.getValue());

        System.out.println(hypermediaLink.getLink());
        return hypermediaLink;
    }
}