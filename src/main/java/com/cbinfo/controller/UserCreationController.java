package com.cbinfo.controller;

import com.cbinfo.dto.form.UserForm;
import com.cbinfo.model.User;
import com.cbinfo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by islabukhin on 20.09.16.
 */
@Controller
public class UserCreationController {

    @Autowired
    private UserService userService;
    private UserForm userForm;

    @RequestMapping(value = "registration", method = RequestMethod.POST)
    public String registration(ModelMap modelMap) {
        return "app";
    }


    protected void setErrorMessage(List<String> errors, ModelMap modelMap) {
        modelMap.addAttribute("errorText", StringUtils.join(errors, System.getProperty("line.separator")));
    }
}
