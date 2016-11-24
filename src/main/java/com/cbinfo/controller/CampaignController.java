package com.cbinfo.controller;

import com.cbinfo.dto.CampaignCreationDTO;
import com.cbinfo.dto.CampaignDTO;
import com.cbinfo.dto.form.FlightForm;
import com.cbinfo.model.Campaign;
import com.cbinfo.model.User;
import com.cbinfo.service.CampaignService;
import com.cbinfo.service.FlightService;
import com.cbinfo.service.UserSessionService;
import com.cbinfo.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/app/campaigns")
public class CampaignController {
    private static final String REDIRECT = "redirect:";
    private static final String APP_PAGE = "/app";
    private static final String EDIT_CAMPAIGN_PAGE = "/app/campaigns/";
    private static final String CREATE_CAMPAIGN_START_PAGE = "/app/campaigns/create/start";
    private static final String CREATE_CAMPAIGN_FLIGHT_PAGE = "/app/campaigns/create/flight";
    private static final String CREATE_CAMPAIGN_FINISH_PAGE = "/app/campaigns/create/finish";

    private static final String APP_VIEW = "application/app";
    private static final String EDIT_FLIGHT_VIEW = "/campaigns/flight/editFlight";
    private static final String CREATE_FLIGHT_VIEW = "/campaigns/flight/createFlight";
    private static final String EDIT_CAMPAIGN_VIEW = "campaigns/editCampaign";
    private static final String CREATE_CAMPAIGN_START_VIEW = "campaigns/creation/createCampaign";
    private static final String CREATE_CAMPAIGN_FLIGHT_VIEW = "campaigns/creation/createCampaignCreateFlight";
    private static final String CREATE_CAMPAIGN_FINISH_VIEW = "campaigns/creation/createCampaignFinish";

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private FlightService flightService;

    @ModelAttribute("user")
    public User user() {
        return userSessionService.getUser();
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getCreateCampaign() {
        return REDIRECT + CREATE_CAMPAIGN_START_PAGE;
    }

    @RequestMapping(value = "/create/start", method = RequestMethod.GET)
    public String getCreateCampaignStart(ModelMap model) {
        model.putIfAbsent("campaign", new CampaignCreationDTO());
        return CREATE_CAMPAIGN_START_VIEW;
    }

    @RequestMapping(value = "/create/start", method = RequestMethod.POST)
    public String postCreateCampaignStart(CampaignCreationDTO campaignCreationDTO, ModelMap modelMap, RedirectAttributes redirectAttributes) {
        try {
            campaignService.checkIfNotExist(campaignCreationDTO.getName());
        } catch (Exception e) {
            modelMap.put("error", e.getMessage());
            return CREATE_CAMPAIGN_START_VIEW;
        }
        redirectAttributes.addFlashAttribute("campaign", campaignCreationDTO);
        return REDIRECT + CREATE_CAMPAIGN_FLIGHT_PAGE;
    }

    @RequestMapping(value = "/create/flight", method = RequestMethod.GET)
    public String getCreateCampaignFlight(ModelMap modelMap) {
        if (modelMap.get("campaign") == null) {
            return REDIRECT + CREATE_CAMPAIGN_START_PAGE;
        }
        modelMap.put("websites", websiteService.findAll());
        return CREATE_CAMPAIGN_FLIGHT_VIEW;
    }

    @RequestMapping(value = "/create/flight", method = RequestMethod.POST)
    public String postCreateCampaignFlight(@Valid CampaignCreationDTO campaignCreationDTO, BindingResult bindingResult, ModelMap modelMap,
                                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("error", "Empty Name!");
            return CREATE_CAMPAIGN_FLIGHT_VIEW;
        }
        redirectAttributes.addFlashAttribute("campaign", campaignCreationDTO);
        return REDIRECT + CREATE_CAMPAIGN_FINISH_PAGE;
    }

    @RequestMapping(value = "/create/finish", method = RequestMethod.GET)
    public String getCreateCampaignFinish() {
        return CREATE_CAMPAIGN_FINISH_VIEW;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String postCreateCampaignFinish(CampaignCreationDTO campaignCreationDTO, ModelMap modelMap) {
        Campaign campaign;
        try {
            campaign = campaignService.saveCampaignCreationDTO(campaignCreationDTO);
        } catch (Exception e) {
            modelMap.put("campaign", campaignCreationDTO);
            modelMap.put("error", "Error! " + e.getMessage());
            return CREATE_CAMPAIGN_FINISH_VIEW;
        }

        return REDIRECT + EDIT_CAMPAIGN_PAGE + campaign.getCampaignId();
    }

    @RequestMapping(value = "/{campaignId}", method = RequestMethod.GET)
    public String getEditCampaign(@PathVariable String campaignId, ModelMap modelMap) {
        modelMap.put("campaign", campaignService.findOneDTO(campaignId));
        modelMap.put("flights", campaignService.getCampaignsFlights(campaignId));
        modelMap.put("campaignId", campaignId);
        return EDIT_CAMPAIGN_VIEW;
    }


    @RequestMapping(value = "/{campaignId}", method = RequestMethod.POST)
    public String postEditCampaign(@PathVariable String campaignId, CampaignDTO campaign, ModelMap modelMap) {
        try {
            campaignService.updateCampaignName(campaignId, campaign);
        } catch (Exception e) {
            modelMap.put("campaign", campaignService.findOneDTO(campaignId));
            modelMap.put("flights", campaignService.getCampaignsFlights(campaignId));
            modelMap.put("campaignId", campaignId);
            modelMap.put("error", "Error! Message: " + e.getMessage());
            return EDIT_CAMPAIGN_VIEW;
        }
        return REDIRECT + EDIT_CAMPAIGN_PAGE + campaignId;
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

    @RequestMapping(value = "/{campaignId}/flights/create", method = RequestMethod.GET)
    public String getCreateFlight(@PathVariable String campaignId, ModelMap modelMap) {
        modelMap.put("campaignId", campaignId);
        return CREATE_FLIGHT_VIEW;
    }

    @RequestMapping(value = "/{campaignId}/flights", method = RequestMethod.POST)
    public String postCreateFlight(@PathVariable String campaignId,@Valid FlightForm flightForm, BindingResult bindingResult, ModelMap modelMap) {
        if(bindingResult.hasErrors()){
            modelMap.put("error", "Name can't be null!");
            return CREATE_FLIGHT_VIEW;
        }

        try {
            flightService.createAndSaveFlight(flightForm, campaignId);
        } catch (Exception e) {
            modelMap.put("error", e.getMessage());
            return CREATE_FLIGHT_VIEW;
        }
        return REDIRECT + EDIT_CAMPAIGN_PAGE + campaignId;
    }

    @RequestMapping(value = "/{campaignId}/flights/{flightId}", method = RequestMethod.GET)
    public String getEditFlight(@PathVariable String campaignId, @PathVariable String flightId, ModelMap modelMap) {
        modelMap.put("flightForm", flightService.findOneForm(flightId));
        modelMap.put("campaignId", campaignId);
        modelMap.put("flightId", flightId);
        modelMap.put("websiteNames", websiteService.findAllNames());
        return EDIT_FLIGHT_VIEW;
    }

    @RequestMapping(value = "/{campaignId}/flights/{flightId}", method = RequestMethod.POST)
    public String postEditFlight(@PathVariable String campaignId, @PathVariable String flightId,
                                 FlightForm flightForm, ModelMap modelMap) {
        try {
            flightService.updateFlight(flightForm, flightId, campaignId);
        } catch (Exception e) {
            modelMap.put("error", e.getMessage());
            return EDIT_FLIGHT_VIEW;
        }
        return REDIRECT + EDIT_CAMPAIGN_PAGE + campaignId;
    }
    // TODO: 22.11.2016 new front-end
}
