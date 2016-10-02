package com.cbinfo.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by islabukhin on 20.09.16.
 */
@Controller
public class LoginController {

    private static final String LOGIN_VIEW = "login";
    private static final String REDIRECT_TO_APP_HANDLER = "redirect:app";

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String getLogin(){
        if(SecurityContextHolder.getContext().getAuthentication()!=null){
            if(!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String)){
                return REDIRECT_TO_APP_HANDLER;
            }
        }
        return LOGIN_VIEW;
    }


}
