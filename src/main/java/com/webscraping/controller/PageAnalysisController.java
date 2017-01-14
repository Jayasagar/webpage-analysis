package com.webscraping.controller;

import com.webscraping.analyse.WebPageAnalyser;
import com.webscraping.analyse.model.AnalysisResult;
import com.webscraping.model.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides the end point for submitted url analysis.
 */
@RestController
@RequestMapping("/scraping")
public class PageAnalysisController {

    @Autowired
    private WebPageAnalyser webPageAnalyser;

    /**
     * Takes the user submitted url, delegates the url for further analysis to {@link WebPageAnalyser} and returns back the
     * {@link ResponseEntity} of entity object {@link AnalysisResult}.
     */
    @RequestMapping(value = "analyse", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AnalysisResult> analysePage(@RequestParam String url) {
        return webPageAnalyser.analysePage(url);
    }
}