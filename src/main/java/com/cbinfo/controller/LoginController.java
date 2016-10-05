package com.cbinfo.controller;

import com.cbinfo.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private UserSessionService userSessionService;

    private static final String LOGIN_VIEW = "login/login";
    private static final String REDIRECT_TO_APP_HANDLER = "redirect:app";

    @RequestMapping(value = "")
    public String getLogin() {
        if (userSessionService.getUser() != null) {
            return REDIRECT_TO_APP_HANDLER;
        }
        return LOGIN_VIEW;
    }
}
