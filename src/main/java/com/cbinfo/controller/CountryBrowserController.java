package com.cbinfo.controller;

import com.cbinfo.model.CountryBrowserData;
import com.cbinfo.service.GeoIPCountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Igor on 29.06.2016.
 */

@Controller
public class CountryBrowserController {
    final Logger logger = LoggerFactory.getLogger(GeoIPCountryService.class);

    @Autowired
    private GeoIPCountryService countryService;


    @RequestMapping(value="/test")
    public String getTestSite(Map<String, Object> model) {
        logger.info("Request to \"test\"");
        return "test";
    }

    @ResponseBody
    @RequestMapping(value="/test/ajax")
    public CountryBrowserData getAjaxCountryBrowserData(@RequestParam("ip") String ip, @RequestHeader("User-Agent")  String userAgent) {
        logger.info("Request to \"/test/ajax\"");
        return countryService.getData(ip, userAgent);
    }


    @ResponseBody
    @RequestMapping(value="/info")
    public CountryBrowserData getCountryBrowserData(@RequestHeader("User-Agent")  String userAgent, HttpServletRequest request) {
        logger.info("Request to \"/info\"");
        String ip = request.getRemoteAddr();
        return countryService.getData(ip, userAgent);
    }



}
