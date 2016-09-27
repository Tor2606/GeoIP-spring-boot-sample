package com.cbinfo.controller;

import com.cbinfo.dto.UserDataDTO;
import com.cbinfo.model.User;
import com.cbinfo.model.UserData;
import com.cbinfo.service.UserDataService;
import com.cbinfo.service.UserService;
import com.cbinfo.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/app")
public class ApplicationController {

    private static final String MAIN_PAGE_VIEW = "application/app";
    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDataService userDataService;

    @ModelAttribute("user")
    public User user() {
        User u = userSessionService.getUser();

        if(u == null) {
            org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            userSessionService.setUser(userService.findByEmail(principal.getUsername()));
        }

        return userSessionService.getUser();
    }

    @RequestMapping("")
    public String getMainPage(ModelMap modelMap){
        List<UserData> resultList = userDataService.getAll();
        modelMap.put("userDataList", resultList);
        return MAIN_PAGE_VIEW;
    }
}