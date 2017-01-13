package com.webscraping.util;

import com.webscraping.util.URIUtils;
import org.junit.Assert;
import org.junit.Test;

public class URIUtilsTest {

    @Test
    public void mailId_as_url_is_a_absolute_url() {
        String url = "mailto:abc@gmail.com";
        Assert.assertTrue(URIUtils.isAbsoluteUrl(url));
    }
}
