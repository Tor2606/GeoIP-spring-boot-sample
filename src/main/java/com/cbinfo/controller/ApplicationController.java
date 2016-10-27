package com.cbinfo.controller;

import com.cbinfo.model.Company;
import com.cbinfo.model.User;
import com.cbinfo.model.UserData;
import com.cbinfo.service.CompanyService;
import com.cbinfo.service.UserDataService;
import com.cbinfo.service.UserService;
import com.cbinfo.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

    @ModelAttribute("user")
    public User user() {
        return userSessionService.getUser();
    }

    @RequestMapping("")
    public String getMainPage(ModelMap modelMap){
        List<UserData> userDataList = userDataService.findAll();
        List<User> users = userService.findAll();
        List<Company> companies = companyService.findAll();
        modelMap.put("userDataList", userDataList);
        modelMap.put("users", users);
        modelMap.put("companies", companies);
        return MAIN_VIEW;
    }
}
