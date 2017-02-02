package com.webscraping.analyse;

import com.webscraping.analyse.model.HypermediaLink;
import com.webscraping.analyse.model.LinkType;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.function.Function;

/**
 * Responsible for creating the {@link HypermediaLink} from the {@link Element}.
 * Basically mapping {@link Element} into {@link HypermediaLink}
 */
@Slf4j
public class ElementToLink implements Function<Element, HypermediaLink> {
    private LinkType type;

    ElementToLink(LinkType type) {
        this.type = type;
    }

    @Override
    public HypermediaLink apply(Element element) {
        HypermediaLink hypermediaLink = new HypermediaLink();
        hypermediaLink.setType(type.getValue());
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

        log.debug(String.format("Created the hyper media link %s of type %s", hypermediaLink.getLink(), hypermediaLink.getType()));
        return hypermediaLink;
    }
}