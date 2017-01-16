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

@Controller
@RequestMapping("/users")
public class CreateUserController {
    private static final String CREATE_USER_VIEW = "users/create";
    private static final String REDIRECT = "redirect:";
    private static final String APP_PAGE = "/app";

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postCreateUser(@Valid UserForm userForm, BindingResult bindingResult, @RequestParam String reenteredPassword,
                                 HttpServletRequest request, @RequestParam String newCompanyName, ModelMap modelMap) {
        if (userService.isEmailRegistered(userForm.getEmail())) {
            modelMap.addAttribute("errorMessage", "Email is not available!");
            modelMap.put("companies", companyService.findAllNames());
            return CREATE_USER_VIEW;
        }
        if (bindingResult.hasErrors()) {
            modelMap.put("companies", companyService.findAllNames());
            return CREATE_USER_VIEW;
        }

        try {
            userService.checkReenteredPassword(userForm.getPassword(), reenteredPassword);
        } catch (Exception e) {
            modelMap.addAttribute("errorMessage", e.getMessage());
            modelMap.put("companies", companyService.findAllNames());
            return CREATE_USER_VIEW;
        }
        userService.setNewCompanyToUserForm(userForm, newCompanyName);
        userService.createUser(userForm, request);
        return REDIRECT + APP_PAGE;
    }

    @RequestMapping("/create")
    public String getCreateUser(ModelMap modelMap) {
        modelMap.put("userForm", new UserForm());
        modelMap.put("companies", companyService.findAllNames());
        return CREATE_USER_VIEW;
    }
}

