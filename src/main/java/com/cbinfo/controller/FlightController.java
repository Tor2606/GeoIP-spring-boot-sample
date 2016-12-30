package com.cbinfo.controller;

import com.cbinfo.dto.form.BannerForm;
import com.cbinfo.dto.form.FlightForm;
import com.cbinfo.model.User;
import com.cbinfo.model.enums.FlightTypes;
import com.cbinfo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Controller
@RequestMapping(value = "/app/flights")
public class FlightController {
    private static final String REDIRECT = "redirect:";
    private static final String APP_PAGE = "/app";
    private static final String CREATE_FLIGHT_START_PAGE = "/app/flights/create/start";
    private static final String CREATE_FLIGHT_MAIN_PAGE = "/app/flights/create/main";
    private static final String CREATE_FLIGHT_BANNER_PAGE = "/app/flights/create/banners";
    private static final String FLIGHTS = "/app/flights/";
    private static final String FLIGHT_ID_PARAM = "?flightId=";
    private static final String EDIT = "/edit";

    private static final String CREATE_FLIGHT_START_VIEW = "campaigns/flights/createFlightStart";
    private static final String CREATE_FLIGHT_MAIN_VIEW = "campaigns/flights/createFlightMain";
    private static final String CREATE_FLIGHT_FINISH_VIEW = "campaigns/flights/createFlightFinish";
    private static final String EDIT_FLIGHT_VIEW = "campaigns/flights/editFlight";
    private static final String CREATE_FLIGHT_BANNER_VIEW = "campaigns/flights/createFlightBanner";

    @Autowired
    private FlightService flightService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private BannerService bannerService;

    @ModelAttribute("user")
    public User user() {
        return userSessionService.getUser();
    }

    @ModelAttribute("loggedTime")
    public Date loggedTime() {
        return userSessionService.getLoggedTime();
    }

    @RequestMapping(value = "/create/start")
    public String getCreateFlightStart(@RequestParam(value = "flightId", required = false) String flightId, ModelMap modelMap) {
        modelMap.put("campaignNames", campaignService.findAllCampaignsNames());
        if (flightId == null) {
            modelMap.put("flightForm", new FlightForm());
            return CREATE_FLIGHT_START_VIEW;
        }
        modelMap.put("flightForm", flightService.findFlightForm(flightId));
        modelMap.put("flightId", flightId);
        return CREATE_FLIGHT_START_VIEW;
    }

    @RequestMapping(value = "/create/start", method = RequestMethod.POST)
    public String postCreateFlightStart(FlightForm flightForm, ModelMap modelMap) {
        if (isBlank(flightForm.getFlightId())) {
            long flightId;
            try {
                flightId = flightService.createFlight(flightForm);
            } catch (Exception e) {
                modelMap.put("campaignNames", campaignService.findAllCampaignsNames());
                modelMap.put("error", "Error:" + e.getMessage());
                return CREATE_FLIGHT_START_VIEW;
            }
            return REDIRECT + CREATE_FLIGHT_MAIN_PAGE + FLIGHT_ID_PARAM + flightId;
        }
        try {
            flightService.updateFlight(flightForm);
        } catch (Exception e) {
            modelMap.put("campaignNames", campaignService.findAllCampaignsNames());
            modelMap.put("error", "Error:" + e.getMessage());
            return CREATE_FLIGHT_START_VIEW;
        }
        return REDIRECT + CREATE_FLIGHT_MAIN_PAGE + FLIGHT_ID_PARAM + flightForm.getFlightId();
    }

    @RequestMapping(value = "/create/main")
    public String getCreateFlightMain(@RequestParam(value = "flightId", required = false) String flightId, ModelMap modelMap) {
        if (isBlank(flightId)) {
            return REDIRECT + CREATE_FLIGHT_START_PAGE;
        }
        modelMap.put("types", newArrayList(FlightTypes.values()));
        modelMap.put("websiteNames", websiteService.findAllWebsiteNamesForCurrentUser());
        modelMap.put("flightForm", flightService.findFlightForm(flightId));
        return CREATE_FLIGHT_MAIN_VIEW;
    }

    @RequestMapping(value = "/create/main", method = RequestMethod.POST)
    public String postCreateFlightMain(FlightForm flightForm, ModelMap modelMap) {
        if (isBlank(flightForm.getFlightId())) {
            return REDIRECT + CREATE_FLIGHT_START_PAGE;
        }
        try {
            flightService.updateFlight(flightForm);
        } catch (Exception e) {
            modelMap.put("error", "Error:" + e.getMessage());
            modelMap.put("types", newArrayList(FlightTypes.values()));
            modelMap.put("websiteNames", websiteService.findAllWebsiteNamesForCurrentUser());
            modelMap.put("flightForm", flightService.findFlightForm(flightForm.getFlightId()));
            return CREATE_FLIGHT_MAIN_VIEW;
        }
        return REDIRECT + CREATE_FLIGHT_BANNER_PAGE + FLIGHT_ID_PARAM + flightForm.getFlightId();
    }

    @RequestMapping(value = "/create/banners")
    public String getCreateFlightBanner(@RequestParam(value = "flightId", required = false) String flightId, ModelMap modelMap) {
        if (isBlank(flightId)) {
            return REDIRECT + CREATE_FLIGHT_START_PAGE;
        }
        modelMap.put("banners", bannerService.findBannerFormByFlight(flightId));
        modelMap.put("flightId", flightId);
        modelMap.put("urlBeginning", flightService.getFlightsURL(flightId));
        return CREATE_FLIGHT_BANNER_VIEW;
    }

    @RequestMapping(value = "/create/banners/create", method = RequestMethod.POST)
    public String postCreateFlightCreateBanner(BannerForm bannerForm, ModelMap modelMap) {
        try {
            bannerService.createBanner(bannerForm);
        } catch (Exception e) {
            fillModelMap(bannerForm, modelMap, e);
            return CREATE_FLIGHT_BANNER_VIEW;
        }

        return REDIRECT + CREATE_FLIGHT_BANNER_PAGE + FLIGHT_ID_PARAM + bannerForm.getFlightId();
    }

    @RequestMapping(value = "/create/{flightId}/banners/{bannerId}/delete")
    public String getCreateFlightDeleteBanner(@PathVariable(value = "flightId") String flightId, @PathVariable(value = "bannerId") String bannerId, ModelMap modelMap) {
        try {
            bannerService.delete(bannerId);
        } catch (Exception e) {
            modelMap.put("error", e.getMessage());
            modelMap.put("banners", bannerService.findBannerFormByFlight(flightId));
            modelMap.put("flightId", flightId);
            modelMap.put("urlBeginning", flightService.getFlightsURL(flightId));
            return CREATE_FLIGHT_BANNER_VIEW;
        }

        return REDIRECT + CREATE_FLIGHT_BANNER_PAGE + FLIGHT_ID_PARAM + flightId;
    }

    @RequestMapping(value = "/create/banners/edit", method = RequestMethod.POST)
    public String postCreateFlightEditBanner(BannerForm bannerForm, ModelMap modelMap, RedirectAttributes redirectAttributes) {
        try {
            bannerService.editBanner(bannerForm);
        } catch (Exception e) {
            fillModelMap(bannerForm, modelMap, e);
            return CREATE_FLIGHT_BANNER_VIEW;
        }
        redirectAttributes.addFlashAttribute("open_banner", bannerForm.getTitle());
        return REDIRECT + CREATE_FLIGHT_BANNER_PAGE + FLIGHT_ID_PARAM + bannerForm.getFlightId();
    }

    private void fillModelMap(BannerForm bannerForm, ModelMap modelMap, Exception e) {
        modelMap.put("error", "Error: " + e.getMessage());
        modelMap.put("banners", bannerService.findBannerFormByFlight(bannerForm.getFlightId()));
        modelMap.put("flightId", bannerForm.getFlightId());
        modelMap.put("urlBeginning", flightService.getFlightsURL(bannerForm.getFlightId()));
    }

    @RequestMapping(value = "/create/finish")
    public String getCreateFlightFinish(@RequestParam(value = "flightId", required = false) String flightId, ModelMap modelMap) {
        if (isBlank(flightId)) {
            return REDIRECT + CREATE_FLIGHT_START_PAGE;
        }
        modelMap.put("flightForm", flightService.findFlightForm(flightId));
        return CREATE_FLIGHT_FINISH_VIEW;
    }

    @RequestMapping(value = "/create/accept")
    public String postCreateFlightFinish() {
        return REDIRECT + APP_PAGE;
    }

    @RequestMapping(value = "/{flightId}/edit")
    public String getEditFlight(@PathVariable(value = "flightId") String flightId, ModelMap modelMap) {
        modelMap.put("types", FlightTypes.values());
        modelMap.put("flightId", flightId);
        modelMap.put("websiteNames", websiteService.findAllWebsiteNamesForCurrentUser());
        modelMap.put("campaignNames", campaignService.findAllCampaignsNames());
        modelMap.put("flightForm", flightService.findFlightForm(flightId));
        return EDIT_FLIGHT_VIEW;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEditFlight(FlightForm flightForm, ModelMap modelMap) {
        try {
            flightService.updateFlight(flightForm);
        } catch (Exception e) {
            modelMap.put("error", "Error:" + e.getMessage());
            return EDIT_FLIGHT_VIEW;
        }
        return REDIRECT + FLIGHTS + flightForm.getFlightId() + EDIT;
    }
}
