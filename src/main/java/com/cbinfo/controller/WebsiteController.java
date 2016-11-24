package com.cbinfo.controller;

import com.cbinfo.dto.form.WebsiteForm;
import com.cbinfo.model.User;
import com.cbinfo.service.UserSessionService;
import com.cbinfo.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/app/websites")
public class WebsiteController {
    private static final String REDIRECT = "redirect:";
    private static final String WEBSITE = "/app/websites/";
    private static final String EDIT = "/edit";
    private static final String APP_PAGE = "/app";

    private static final String EDIT_WEBSITE_VIEW = "website/editWebsite";
    private static final String CREATE_WEBSITE_VIEW = "website/createWebsite";
    private static final String APP_VIEW = "application/app";

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private UserSessionService userSessionService;

    @ModelAttribute("user")
    public User user() {
        return userSessionService.getUser();
    }

    @RequestMapping(value = "/create")
    public String getCreateWebsite(){
        return CREATE_WEBSITE_VIEW;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postCreateWebsite(WebsiteForm websiteForm, ModelMap modelMap){
        try{
            websiteService.createWebsite(websiteForm);
        }catch (Exception e){
            modelMap.put("error", "Error:" + e.getMessage());
            return CREATE_WEBSITE_VIEW;
        }
        return REDIRECT + APP_PAGE;
    }

    @RequestMapping(value = "/{websiteId}")
    public String getEditWebsite(@PathVariable String websiteId, ModelMap modelMap){
        modelMap.put("websiteForm", websiteService.findOneForm(websiteId));
        modelMap.put("websiteId", websiteId);
        return EDIT_WEBSITE_VIEW;
    }

    @RequestMapping(value = "/{websiteId}", method = RequestMethod.POST)
    public String postEditWebsite(@PathVariable String websiteId, WebsiteForm websiteForm, ModelMap modelMap){
        try{
            websiteService.editWebsite(websiteId, websiteForm);
        }catch (Exception e){
            modelMap.put("websiteForm", websiteForm);
            modelMap.put("websiteId", websiteId);
            modelMap.put("error", "Error: " + e.getMessage());
            return EDIT_WEBSITE_VIEW;
        }
        return REDIRECT + WEBSITE + websiteId + EDIT;
    }

    @RequestMapping(value = "/{websiteId}/delete", method = RequestMethod.GET)
    public String deleteCampaign(@PathVariable String websiteId, ModelMap modelMap) {
        try {
            websiteService.deleteWebsite(websiteId);
        } catch (Exception e) {
            modelMap.put("exception", "Exception on deleting");
            return APP_VIEW;
        }
        return REDIRECT + APP_PAGE;
    }
}
