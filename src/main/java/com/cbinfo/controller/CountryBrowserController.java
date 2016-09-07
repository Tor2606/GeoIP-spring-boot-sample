package com.cbinfo.controller;

import com.cbinfo.dto.UserDataDto;
import com.cbinfo.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;

/**
 * Created by Igor on 29.06.2016.
 */

@Controller
public class CountryBrowserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataService.class);

    @Autowired
    private UserDataService userDataService;

    @RequestMapping(value="/info")
    @ResponseBody
    public UserDataDto getCountryBrowserData(HttpServletRequest request) {
        LOGGER.info("Request to \"/info\"");
        return userDataService.getData(request);
    }
}
