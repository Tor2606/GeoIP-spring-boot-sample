package com.cbinfo.controller;

import com.cbinfo.dto.UserDataDTO;
import com.cbinfo.service.UserDataService;
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

    @Autowired
    private UserDataService userDataService;

    @RequestMapping("/info")
    @ResponseBody
    public UserDataDTO getCountryBrowserData(HttpServletRequest request) {
        UserDataDTO result = userDataService.getDataAndSave(request);
        return result;
    }
}
