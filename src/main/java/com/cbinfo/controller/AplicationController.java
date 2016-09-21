package com.cbinfo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by islabukhin on 20.09.16.
 */
@Controller
public class AplicationController {

    @RequestMapping(value = "app", method = RequestMethod.GET)
    public ModelAndView mainPage(){
        ModelAndView model = new ModelAndView();
        model.setViewName("app");
        return model;
    }
}
