package com.cbinfo.controller;

import com.cbinfo.dto.form.UserForm;
import com.cbinfo.service.CompanyService;
import com.cbinfo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final String CREATE_USER_VIEW = "users/create";
    private static final String REDIRECT = "redirect:";
    private static final String APP_PGE = "/app";
    private static final String EDIT_USER_VIEW = "users/edit";
    private static final String LOGIN_PAGE = "/";

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postCreateUser(@Valid UserForm userForm, BindingResult bindingResult,  @RequestParam("reenteredPassword") String reenteredPassword,
                                 HttpServletRequest request, @RequestParam("newCompanyName") String newCompanyName, ModelMap modelMap) {
        if (userService.isEmailRegistered(userForm.getEmail())) {
            modelMap.addAttribute("errorMessage", "Email is not available!");
            return CREATE_USER_VIEW;
        }
        if (bindingResult.hasErrors()) {
            return CREATE_USER_VIEW;
        }

        try {
            userService.checkReenteredPassword(userForm.getPassword(), reenteredPassword);
        } catch (Exception e) {
            modelMap.addAttribute("errorMessage", e.getMessage());
            return CREATE_USER_VIEW;
        }

        setNewCompany(userForm, newCompanyName);
        userService.createUser(userForm, request.getRemoteAddr());
        return REDIRECT + LOGIN_PAGE;
    }

    @RequestMapping("/create")
    public String getCreateUser(ModelMap modelMap) {
        modelMap.put("userForm", new UserForm());
        modelMap.put("companies", companyService.findAll());
        return CREATE_USER_VIEW;
    }

    @RequestMapping("/edit")
    public String getEditUser(ModelMap modelMap) {
        modelMap.put("userForm", userService.getCurrentSessionUserToForm());
        return EDIT_USER_VIEW;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEditUser(@Valid UserForm userForm, BindingResult bindingResult, @RequestParam("reenteredPassword") String reenteredPassword, ModelMap modelMap) {
        if (bindingResult.hasFieldErrors("email")) {
            return EDIT_USER_VIEW;
        }else if (bindingResult.hasFieldErrors("password")&&isNotBlank(userForm.getPassword())){
            return EDIT_USER_VIEW;
        }
        try {
            userService.updateCurrentUser(userForm, reenteredPassword);
        } catch (Exception e) {
            modelMap.addAttribute("errorMessage", e.getMessage());
            return EDIT_USER_VIEW;
        }
        return REDIRECT + APP_PGE;
    }

    private void setNewCompany(@Valid UserForm userForm, @RequestParam("newCompanyName") String newCompanyName) {
        if(isNotBlank(newCompanyName)){
            userForm.setCompanyName(newCompanyName);
        }
    }
}

