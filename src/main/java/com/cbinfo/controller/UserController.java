package com.cbinfo.controller;

import com.cbinfo.dto.form.UserForm;
import com.cbinfo.model.User;
import com.cbinfo.service.UserService;
import com.cbinfo.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final String CREATE_USER_VIEW = "users/create";
    private static final String REDIRECT_APP = "redirect:/app";
    private static final String EDIT_USER_VIEW = "users/edit";
    private static final String REDIRECT_LOGIN = "redirect:/login";

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postCreateUser(@Valid UserForm userForm, BindingResult bindingResult, HttpServletRequest request, ModelMap modelMap) {
        if (userService.isEmailRegistered(userForm.getEmail())) {
            modelMap.addAttribute("errorMessage", "Email is not available!");
            return CREATE_USER_VIEW;
        }
        if (bindingResult.hasErrors()) {
            return CREATE_USER_VIEW;
        }
        userService.createUser(userForm, request.getRemoteAddr());
        return REDIRECT_LOGIN;
    }

    @RequestMapping("/create")
    public String getCreateUser(ModelMap modelMap) {
        modelMap.put("userForm", new UserForm());
        return CREATE_USER_VIEW;
    }

    @RequestMapping("/edit")
    public String getEditUser(ModelMap modelMap) {
        modelMap.put("userForm", userService.getCurrentSessionUserToForm());
        return EDIT_USER_VIEW;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEditUser(@Valid UserForm userForm, BindingResult bindingResult, ModelMap modelMap) {
        if(notValidFields(bindingResult, userForm)) {
            return EDIT_USER_VIEW;
        }
        try {
            userService.updateCurrentUser(userForm);
        } catch (Exception e) {
            modelMap.addAttribute("errorMessage", e.getMessage());
            return EDIT_USER_VIEW;
        }
        return REDIRECT_APP;
    }

    private boolean notValidFields(BindingResult bindingResult, UserForm userForm) {
        if(bindingResult.hasErrors()&&isNotBlank(userForm.getPassword())) {
            return true;}
        return false;
    }
}
