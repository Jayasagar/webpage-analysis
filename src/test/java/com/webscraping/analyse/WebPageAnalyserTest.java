package com.webscraping.analyse;

import com.webscraping.analyse.model.AnalysisResult;
import com.webscraping.analyse.model.HTMLVersion;
import com.webscraping.analyse.model.HypermediaLink;
import com.webscraping.analyse.model.LinkGroup;
import com.webscraping.model.*;
import com.webscraping.ErrorMessages;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class WebPageAnalyserTest {

    @Test
    public void invalid_url_should_return_error_message() throws URISyntaxException {
        ResponseEntity<AnalysisResult> result = new WebPageAnalyser().analysePage("htp://google.com");

        assertFalse(result.isStatus());
        Assert.assertEquals(result.getMessage(), ErrorMessages.INVALID_URL);
    }

    @Test
    public void through_timeout_response_error_if_unable_to_load_in_given_time() throws URISyntaxException {
        // http://deelay.me/6000?http://google.com
        // http://ec2-52-213-11-153.eu-west-1.compute.amazonaws.com
        ResponseEntity<AnalysisResult> result = new WebPageAnalyser().analysePage("http://deelay.me/6000?http://google.com");

        assertEquals(result.getMessage(), ErrorMessages.TIMEOUT_ERROR);
    }

    @Test
    public void valid_url_should_return_success_status() throws URISyntaxException {
        ResponseEntity<AnalysisResult> result = new WebPageAnalyser().analysePage("http://google.com");

        assertTrue(result.isStatus());
    }

    @Test
    public void should_return_html_5_version() throws IOException {
        Document document = Jsoup.connect("http://google.com").get();

        String pageVersion = new WebPageAnalyser().getPageVersion(document);

        assertEquals(pageVersion, HTMLVersion.HTML5.getValue());
    }

    @Test
    public void should_return_html_4_version() {
        // Sample html 4
        String html4Content = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">" +
                                "<html><body> <div> Hi </div> </body></html>";
        Document document = Jsoup.parse(html4Content);

        // Act
        String pageVersion = new WebPageAnalyser().getPageVersion(document);

        assertEquals(pageVersion, HTMLVersion.HTML4.getValue());
    }

    @Test
    public void should_return_page_title() throws IOException {
        Document document = Jsoup.connect("http://google.com").get();

        String pageTitle = new WebPageAnalyser().getPageTitle(document);

        assertEquals(pageTitle, "Google");
    }

    @Test
    public void should_return_empty_page_title() throws IOException {
        Document document = Jsoup.parse("<html><body> <div> Hi </div> </body></html>");

        String pageTitle = new WebPageAnalyser().getPageTitle(document);

        assertEquals(pageTitle, "");
    }

    @Test
    public void should_return_all_hypermedia_links() throws IOException, URISyntaxException {
        // http://www.ycombinator.com/
        // http://news.ycombinator.com/
        // www.google.com
        Document document = Jsoup.connect("http://news.ycombinator.com/").get();

        Elements links1 = document.select("a[href]");

        Map<LinkGroup, Set<HypermediaLink>> links = new WebPageAnalyser().getHypermediaLinks("http://news.ycombinator.com/", document);

        assertNotNull(links);
    }

    @Test
    public void heading_number_group_by_level_count_should_be_empty() {
        Document document = Jsoup.parse("<html><body> </body></html>");

        Map<String, Long> headingGroupByLevel = new WebPageAnalyser().getHeadingGroupByLevel(document);

        assertTrue(headingGroupByLevel.isEmpty());
    }

    @Test
    public void heading_number_group_by_level_count_should_work() {
        Document document = Jsoup.parse("<html><body> <h1> Hi </h1> <h2>hi2</h2> <h2>hi2.1</h2> </body></html>");

        Map<String, Long> headingGroupByLevel = new WebPageAnalyser().getHeadingGroupByLevel(document);

        assertEquals(headingGroupByLevel.get("h1").longValue(), 1l);
        assertEquals(headingGroupByLevel.get("h2").longValue(), 2l);
    }

    @Test
    public void page_with_password_input_should_be_login_form() {
        Document document = Jsoup.parse("<html><body> <input type=\"password\"/> </body></html>");

        boolean isLoginForm = new WebPageAnalyser().isLoginForm(document);

        assertTrue(isLoginForm);
    }
}
