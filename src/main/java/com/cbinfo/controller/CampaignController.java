package com.cbinfo.controller;

import com.cbinfo.dto.form.FlightForm;
import com.cbinfo.model.Campaign;
import com.cbinfo.model.User;
import com.cbinfo.service.CampaignService;
import com.cbinfo.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/app/campaigns")
public class CampaignController {
    private static final String REDIRECT = "redirect:";
    private static final String APP_PAGE = "/app";
    private static final String EDIT_CAMPAIGN_PAGE = "/app/campaigns/";

    private static final String APP_VIEW = "application/app";
    private static final String EDIT_FLIGHT_VIEW = "/campaigns/editCampaignFlights";
    private static final String CREATE_FLIGHT_VIEW = "/campaigns/createFlight";
    private static final String EDIT_CAMPAIGN_VIEW = "campaigns/editCampaign";
    private static final String CREATE_CAMPAIGN_VIEW = "campaigns/creation/createCampaign";

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private UserSessionService userSessionService;

    @ModelAttribute("user")
    public User user() {
        return userSessionService.getUser();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postCreateCampaign(String campaignName, ModelMap modelMap) {
        Campaign campaign;
        try {
            campaign = campaignService.createCampaign(campaignName);
        } catch (Exception e) {
            modelMap.put("error", e.getMessage());
            return CREATE_CAMPAIGN_VIEW;
        }
        return REDIRECT + EDIT_CAMPAIGN_PAGE + campaign.getCampaignId();
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getCreateCampaign(){
        return CREATE_CAMPAIGN_VIEW;
    }

    @RequestMapping(value = "/{campaignId}", method = RequestMethod.GET)
    public String getCampaignFlights(@PathVariable String campaignId, ModelMap modelMap){
        modelMap.put("campaign", campaignService.findOne(campaignId));
        return EDIT_CAMPAIGN_VIEW;
    }

    @RequestMapping(value = "/{campaignId}/delete", method = RequestMethod.GET)
    public String deleteCampaign(@PathVariable String campaignId,  ModelMap modelMap){
        try{
            campaignService.deleteCampaign(campaignId);
        }catch (Exception e){
            modelMap.put("exception", "Exception on deleting");
            return APP_VIEW;
        }
        return REDIRECT + APP_PAGE;
    }

    @RequestMapping(value = "/{campaignId}/flights/create", method = RequestMethod.GET)
    public String getCreateFlight(@PathVariable String campaignId, ModelMap modelMap){
        modelMap.put("campaign", campaignService.findOne(campaignId));
        return CREATE_FLIGHT_VIEW;
    }

    @RequestMapping(value = "/{campaignId}/flights", method = RequestMethod.POST)
    public String postCreateFlight(@PathVariable String campaignId, FlightForm flightForm, ModelMap modelMap){
        try{
            campaignService.createFlight(flightForm, campaignId);
        }catch (Exception e){
            modelMap.put("Error", e.getMessage());
            return CREATE_FLIGHT_VIEW;
        }
        return REDIRECT + EDIT_CAMPAIGN_PAGE + campaignId;
    }
}
