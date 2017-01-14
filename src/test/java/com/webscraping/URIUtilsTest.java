package com.webscraping;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class URIUtilsTest {

    @Test
    public void get_uri_should_encode_the_given_url() throws MalformedURLException, URISyntaxException {
        String url = "https://fonts.googleapis.com/css?family=Varela+Round|Montserrat:400,700";
        URI uri = URIUtils.getUri(url);

        Assert.assertEquals(uri.toString(), "https://fonts.googleapis.com/css?family=Varela+Round%7CMontserrat:400,700");
    }

    @Test(expected = URISyntaxException.class)
    public void get_uri_should_throw_exception_on_invalid_return_url() throws MalformedURLException, URISyntaxException {
        String url = "mailto:abc@gmail.com";
        URIUtils.getUri(url);
    }

    @Test
    public void domain_name_without_www_should_return_complete_domain() {
        String url = "http://google.com";
        assertEquals(URIUtils.getDomainName(url), "google.com");
    }

    @Test
    public void invalid_url__should_result_in_empty_domain_name() {
        String url = "google.com";
        assertEquals(URIUtils.getDomainName(url), "");
    }

    @Test
    public void with_www_prefix_should_result_in_without_www_in_domain_name() {
        String url = "http://www.google.com";
        assertEquals(URIUtils.getDomainName(url), "google.com");
    }

    @Test
    public void mailId_as_url_is_not_a_absolute_url() {
        String url = "mailto:abc@gmail.com";
        assertFalse(URIUtils.isAbsoluteUrl(url));
    }
}
