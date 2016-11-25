package com.cbinfo.controller;

import com.cbinfo.dto.CampaignDTO;
import com.cbinfo.model.User;
import com.cbinfo.service.CampaignService;
import com.cbinfo.service.FlightService;
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

    private static final String APP_VIEW = "application/app";
    private static final String EDIT_CAMPAIGN_VIEW = "campaigns/editCampaign";
    private static final String CREATE_CAMPAIGN_VIEW = "campaigns/createCampaign";
    private static final String CAMPAIGN_FLIGHT_LIST = "campaign/flights/flightList";

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private FlightService flightService;

    @ModelAttribute("user")
    public User user() {
        return userSessionService.getUser();
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getCreateCampaign() {
        return CREATE_CAMPAIGN_VIEW;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postCreateCampaignStart(CampaignDTO campaignDTO, ModelMap modelMap) {
        try {
            campaignService.checkIfNotExist(campaignDTO.getCampaignName());
            campaignService.createCampaign(campaignDTO);
        } catch (Exception e) {
            modelMap.put("error", e.getMessage());
            return CREATE_CAMPAIGN_VIEW;
        }
        return REDIRECT + APP_PAGE;
    }

    @RequestMapping(value = "/{campaignId}/edit", method = RequestMethod.GET)
    public String getEditCampaign(@PathVariable String campaignId, ModelMap modelMap) {
        modelMap.put("campaign", campaignService.findOneDTO(campaignId));
        return EDIT_CAMPAIGN_VIEW;
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEditCampaign(CampaignDTO campaignDTO, ModelMap modelMap) {
        try {
            campaignService.updateCampaignName(campaignDTO);
        } catch (Exception e) {
            modelMap.put("campaignDTO", campaignDTO);
            modelMap.put("error", "Error: " + e.getMessage());
            return EDIT_CAMPAIGN_VIEW;
        }
        return REDIRECT + APP_PAGE;
    }

    @RequestMapping(value = "/{campaignId}/delete", method = RequestMethod.GET)
    public String deleteCampaign(@PathVariable String campaignId, ModelMap modelMap) {
        try {
            campaignService.deleteCampaign(campaignId);
        } catch (Exception e) {
            modelMap.put("exception", "Exception on deleting");
            return APP_VIEW;
        }
        return REDIRECT + APP_PAGE;
    }

    @RequestMapping(value = "/{campaignId}")
    public String getCampaignFlights(@PathVariable String campaignId, ModelMap modelMap) {
        modelMap.put("campaignName", campaignService.findCampaign(campaignId).getCampaignName());
        modelMap.put("flights", flightService.findFlightFormsByCampaign(campaignId));
        return CAMPAIGN_FLIGHT_LIST;
    }
}
