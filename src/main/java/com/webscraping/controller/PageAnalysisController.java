package com.webscraping.controller;

import com.webscraping.analyse.model.AnalysisResult;
import com.webscraping.model.ResponseEntity;
import com.webscraping.ErrorMessages;
import com.webscraping.analyse.WebPageAnalyser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/scraping")
public class PageAnalysisController {

    @Autowired
    private WebPageAnalyser webPageAnalyser;

    @RequestMapping(value = "analyse", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AnalysisResult> analysePage(@RequestParam String url) {
        try {
            return webPageAnalyser.analysePage(url);
        } catch (URISyntaxException e) {
            return ResponseEntity.error(ErrorMessages.PARSE_ERROR);
        }
    }
}