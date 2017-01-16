package com.cbinfo.controller;

import com.cbinfo.dto.form.UserForm;
import com.cbinfo.model.User;
import com.cbinfo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

import static com.cbinfo.model.enums.UserRoles.ROLE_ADMIN;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Controller
@RequestMapping(value = "/app")
public class ApplicationController {

    private static final String MAIN_VIEW = "application/app";

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private CampaignService campaignService;

    @ModelAttribute("user")
    public User user() {
        return userSessionService.getUser();
    }

    @ModelAttribute("loggedTime")
    public Date loggedTime() {
        return userSessionService.getLoggedTime();
    }

    @RequestMapping("")
    public String getMainPage(ModelMap modelMap) {
        if (user().getRole().equals(ROLE_ADMIN)) {
            modelMap.put("userDataList", userDataService.findAll());
            modelMap.put("users", userService.findAll());
            modelMap.put("companies", companyService.findAll());
        }
        modelMap.put("campaigns", campaignService.findAllCurrentUserCampaigns());
        modelMap.put("websites", websiteService.findAllWebsitesForCurrentUser());
        return MAIN_VIEW;
    }

    @ResponseBody
    @RequestMapping("/users/edit")
    public UserForm getEditUser() {
        return userService.getCurrentSessionUserToForm();
    }

    @ResponseBody
    @RequestMapping(value = "/users/edit", method = RequestMethod.POST)
    public ResponseEntity postEditUser(@Valid UserForm userForm, BindingResult bindingResult, @RequestParam String reenteredPassword) {
        userService.getCurrentSessionUserToForm();
        if (bindingResult.hasFieldErrors("email")) {
            return new ResponseEntity<>(bindingResult.getFieldError("email").getDefaultMessage(), HttpStatus.BAD_REQUEST);
        } else if (isNotBlank(userForm.getPassword()) && bindingResult.hasFieldErrors("password")) {
            return new ResponseEntity<>(bindingResult.getFieldError("password").getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        try {
            userService.updateCurrentUser(userForm, reenteredPassword);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @ResponseBody
    @RequestMapping(value = "/users/delete/{userId}", method = RequestMethod.GET)
    public ResponseEntity deleteUser(@PathVariable String userId) {
        try {
            userService.delete(userId);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
