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

/**
 * Created by islabukhin on 20.09.16.
 */
@Controller
@RequestMapping("/users")
public class UserController {
    private static final String CREATE_USER_VIEW = "users/create";
    private static final String REDIRECT_APP = "redirect:/app";
    private static final String EDIT_USER_VIEW = "users/edit";

    @Autowired
    private UserSessionService userSessionService;

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
        String ip = request.getRemoteAddr();
        userService.createUser(userForm, ip);
        return REDIRECT_APP;
    }

    @RequestMapping("/createPage")
    public String getCreateUser(UserForm userForm) {
        return CREATE_USER_VIEW;
    }

    @RequestMapping("/editPage")
    public String getEditUser(ModelMap modelMap) {
        modelMap.put("userForm", getCurrentSessionUserToForm());
        return EDIT_USER_VIEW;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEditUser(@Valid UserForm userForm, BindingResult bindingResult, HttpServletRequest request, ModelMap modelMap) {
        if (userService.isEmailRegistered(userForm.getEmail())) {
            modelMap.addAttribute("errorMessage", "Email is not available!");
            return EDIT_USER_VIEW;
        }
        if (bindingResult.hasErrors()) {
            return EDIT_USER_VIEW;
        }
        try {
            userService.updateCurrentUser(userForm);
        } catch (Exception e) {
            modelMap.addAttribute("errorMessage", e.getMessage());
        }
        return REDIRECT_APP;
    }

    private UserForm getCurrentSessionUserToForm() {
        User u = userSessionService.getUser();

        if(u == null) {
            org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userSessionService.setUser(userService.findByEmail(principal.getUsername()));
            u = userSessionService.getUser();
        }

        return makeUserFormFromUser(u);
    }

    private UserForm makeUserFormFromUser(User user){
        UserForm userForm = new UserForm();
        userForm.setEmail(user.getEmail());
        userForm.setPassword(user.getPassword());
        userForm.setFirstName(user.getFirstName());
        userForm.setLastName(user.getLastName());
        return userForm;
    }
}
