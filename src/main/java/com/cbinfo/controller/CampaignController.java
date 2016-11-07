package com.cbinfo.controller;

import com.cbinfo.model.Campaign;
import com.cbinfo.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/app/campaigns")
public class CampaignController {
    private static final String REDIRECT = "redirect:";
    private static final String EDITVIEW = "campaigns/editCampaignFlights";
    private static final String EDITPAGE = "/app/campaigns/edit";
    private static final String CREATE_CAMPAIGN = "/app/campaigns";

    @Autowired
    private CampaignService campaignService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postCreateCampaign(String campaignName, ModelMap modelMap) {
        Campaign campaign;
        try {
            campaign = campaignService.createCampaign(campaignName);
        } catch (Exception e) {
            modelMap.put("exception", e.getMessage());
            return CREATE_CAMPAIGN;
        }
        modelMap.put("campaign", campaign);
        return EDITPAGE;
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String getCreateCampaign(){
        return CREATE_CAMPAIGN;
    }

}
