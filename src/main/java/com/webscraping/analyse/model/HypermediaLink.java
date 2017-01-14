package com.webscraping.analyse.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jsoup.nodes.Element;

import java.util.function.Function;

/**
 * Represents the hypermedia resource in the page. It could be anchor tag, src tag.
 */
@Getter @Setter @ToString(of = {"link", "type"})
@EqualsAndHashCode(of = "link")
public class HypermediaLink {
    private String link;
    private String type;
    private String text;
    private boolean reachable;
    /**
     * It holds the information about link, whether it is reachable or not. If not then reason for why.
     */
    private String remark;


}
