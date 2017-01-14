package com.webscraping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Maps to '/' endpoint so that when user open the application, it returns the index page.
 */
@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String index() {
        return "index.html";
    }

}