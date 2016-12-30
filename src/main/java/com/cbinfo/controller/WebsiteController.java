package com.cbinfo.controller;

import com.cbinfo.model.User;
import com.cbinfo.service.UserSessionService;
import com.cbinfo.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping(value = "/app/websites")
public class WebsiteController {
    private static final String REDIRECT = "redirect:";
    private static final String WEBSITE = "/app/websites/";
    private static final String EDIT = "/edit";
    private static final String APP_PAGE = "/app";

    private static final String EDIT_WEBSITE_VIEW = "website/editWebsite";
    private static final String CREATE_WEBSITE_VIEW = "website/createWebsite";

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private UserSessionService userSessionService;

    @ModelAttribute("user")
    public User user() {
        return userSessionService.getUser();
    }

    @ModelAttribute("loggedTime")
    public Date loggedTime() {
        return userSessionService.getLoggedTime();
    }

    @RequestMapping(value = "/create")
    public String getCreateWebsite(){
        return CREATE_WEBSITE_VIEW;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postCreateWebsite(String name, ModelMap modelMap, HttpServletRequest request){
        try{
            websiteService.createWebsite(name);
        }catch (Exception e){
            modelMap.put("error", "Error:" + e.getMessage());
            return CREATE_WEBSITE_VIEW;
        }
        return REDIRECT + APP_PAGE;
    }

    @RequestMapping(value = "/ajax-create", method = RequestMethod.POST)
    public ResponseEntity postCreateWebsite(@RequestParam(name = "websiteName") String name){
        try{
            websiteService.createWebsite(name);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{websiteId}")
    public String getEditWebsite(@PathVariable String websiteId, ModelMap modelMap){
        modelMap.put("name", websiteService.findWebsiteNameById(websiteId));
        modelMap.put("websiteId", websiteId);
        return EDIT_WEBSITE_VIEW;
    }

    @RequestMapping(value = "/{websiteId}", method = RequestMethod.POST)
    public String postEditWebsite(@PathVariable String websiteId, String name, ModelMap modelMap){
        try{
            websiteService.editWebsite(websiteId, name);
        }catch (Exception e){
            modelMap.put("name", name);
            modelMap.put("websiteId", websiteId);
            modelMap.put("error", "Error: " + e.getMessage());
            return EDIT_WEBSITE_VIEW;
        }
        return REDIRECT + WEBSITE + websiteId + EDIT;
    }

    @RequestMapping(value = "/{websiteId}/delete", method = RequestMethod.GET)
    public String deleteWebsite(@PathVariable String websiteId, RedirectAttributes redirectAttributes) {
        try {
            websiteService.deleteWebsite(websiteId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Exception on website deleting! Impossible to delete website!");
        }
        return REDIRECT + APP_PAGE;
    }
}
