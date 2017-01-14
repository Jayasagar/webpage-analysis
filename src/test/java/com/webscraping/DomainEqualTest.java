package com.webscraping;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class DomainEqualTest {

    private String submittedUrl;
    private String scannedUrl;
    private boolean expectedResult;

    public DomainEqualTest(String submittedUrl, String scannedUrl, boolean expectedResult) {
        this.submittedUrl = submittedUrl;
        this.scannedUrl = scannedUrl;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection data() {
    	/* First element is the submitted URL, second is the scanned URL and last is expected result */
        List uriToBeTested =  Arrays.asList(new Object[][] {
                {"http://www.google.com", "http://google.com", true},
                {"http://google.com", "http://www.google.com", true},
                {"http://www.google.com", "http://support.google.com", true},
                {"http://google.com", "http://google.de", false},
                {"http://google.com", "www.google.de", false} // scanned url is not stats with http
        });

        return uriToBeTested;
    }

    @Test
    public void is_same_domain_test() throws IOException, URISyntaxException {
        Assert.assertEquals(URIUtils.isSameDomain(submittedUrl, scannedUrl), expectedResult);
    }
}
