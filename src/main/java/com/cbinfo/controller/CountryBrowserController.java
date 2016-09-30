package com.cbinfo.controller;

import com.cbinfo.dto.UserDataDTO;
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

    @RequestMapping("/info")
    @ResponseBody
    public UserDataDTO getCountryBrowserData(HttpServletRequest request) {
        UserDataDTO result = userDataService.getData(request);
        userDataService.saveUserData(result);
        //// TODO: 27.09.16 make method in contr, + change to saveUserData
        return result;
    }
}
