package com.cbinfo.controller;

import com.cbinfo.model.User;
import com.cbinfo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/app")
public class ApplicationController {

    private static final String MAIN_VIEW = "application/app";

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CampaignService campaignService;

    @ModelAttribute("user")
    public User user() {
        return userSessionService.getUser();
    }

    @RequestMapping("")
    public String getMainPage(ModelMap modelMap) {
        modelMap.put("userDataList", userDataService.findAll());
        modelMap.put("users", userService.findAll());
        modelMap.put("companies", companyService.findAll());
        modelMap.put("campaigns", campaignService.findAllAvailableCampaigns());
        return MAIN_VIEW;
    }
}
