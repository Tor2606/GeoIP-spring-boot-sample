package com.cbinfo.controller;

import com.cbinfo.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/")
public class LoginController {

    @Autowired
    private UserSessionService userSessionService;

    private static final String LOGIN_VIEW = "application/login";
    private static final String REDIRECT_TO_APP_HANDLER = "redirect:app";

    @RequestMapping("")
    public String getLogin() {
        if (userSessionService.getUser() != null) {
            return REDIRECT_TO_APP_HANDLER;
        }
        return LOGIN_VIEW;
    }
}
