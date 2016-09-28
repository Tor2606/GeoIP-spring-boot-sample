package com.cbinfo.controller;

import com.cbinfo.dto.form.UserForm;
import com.cbinfo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by islabukhin on 20.09.16.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private static final String CREATE_USER_VIEW = "users/create";
    private static final String REDIRECT_APP = "redirect:app";
    private static final String EDIT_USER_VIEW ="edit";

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postCreateUser(@Valid @ModelAttribute("userForm") UserForm userForm, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        userService.createUser(userForm, ip);
        return REDIRECT_APP;
    }

    @RequestMapping(value = "/createPage")
    public String getCreateUser(UserForm userForm){
        return CREATE_USER_VIEW;
    }

    @RequestMapping(value = "editPage")
    public String getEditUser(){return EDIT_USER_VIEW;}


    protected void setErrorMessage(List<String> errors, ModelMap modelMap) {
        modelMap.addAttribute("/errorText", StringUtils.join(errors, System.getProperty("line.separator")));
    }
}
