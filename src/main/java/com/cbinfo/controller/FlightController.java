package com.cbinfo.controller;

import com.cbinfo.dto.enums.FlightTypeEnum;
import com.cbinfo.dto.form.FlightForm;
import com.cbinfo.model.User;
import com.cbinfo.service.CampaignService;
import com.cbinfo.service.FlightService;
import com.cbinfo.service.UserSessionService;
import com.cbinfo.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Controller
@RequestMapping(value = "/app/flights")
public class FlightController {
    private static final String REDIRECT = "redirect:";
    private static final String APP_PAGE = "/app";
    private static final String CREATE_FLIGHT_START_PAGE = "/app/flights/create/start";
    private static final String CREATE_FLIGHT_MAIN_PAGE = "/app/flights/create/main";
    private static final String CREATE_FLIGHT_FINISH_PAGE = "/app/flights/create/finish";
;

    private static final String CREATE_FLIGHT_START_VIEW = "campaigns/flights/createFlightStart";
    private static final String CREATE_FLIGHT_MAIN_VIEW = "campaigns/flights/createFlightMain";
    private static final String CREATE_FLIGHT_FINISH_VIEW = "campaigns/flights/createFlightFinish";
    private static final String FLIGHT_ID_PARAM = "?flightId=";
    private static final String EDIT_FLIGHT_VIEW = "campaigns/flights/editFlight";

    @Autowired
    private FlightService flightService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private UserSessionService userSessionService;

    @ModelAttribute("user")
    public User user() {
        return userSessionService.getUser();
    }

    @RequestMapping(value = "/create/start")
    public String getCreateFlightStart(@RequestParam(value = "flightId", required = false) String flightId, ModelMap modelMap) {
        modelMap.put("campaignNames", campaignService.getAllCampaignsNames());
        if (flightId == null) {
            modelMap.put("flightForm", new FlightForm());
            return CREATE_FLIGHT_START_VIEW;
        }
        modelMap.put("flightForm", flightService.findOneForm(flightId));
        modelMap.put("flightId", flightId);
        return CREATE_FLIGHT_START_VIEW;
    }

    @RequestMapping(value = "/create/start", method = RequestMethod.POST)
    public String postCreateFlightStart(FlightForm flightForm, ModelMap modelMap) throws ParseException {
        if (checkIdEqualsZero(flightForm)) {
            long flightId;
            try {
                flightId = flightService.createFlight(flightForm);
            } catch (Exception e) {
                modelMap.put("error", "Error:" + e.getMessage());
                return CREATE_FLIGHT_START_VIEW;
            }
            return REDIRECT + CREATE_FLIGHT_MAIN_PAGE + FLIGHT_ID_PARAM + flightId;
        }
        flightService.updateFlight(flightForm);
        return REDIRECT + CREATE_FLIGHT_MAIN_PAGE + FLIGHT_ID_PARAM + flightForm.getFlightId();
    }

    @RequestMapping(value = "/create/main")
    public String getCreateFlightMain(@RequestParam(value = "flightId", required = false) String flightId, ModelMap modelMap) throws ParseException {
        if (flightId==null) {
            return REDIRECT + CREATE_FLIGHT_START_PAGE;
        }
        modelMap.put("types", FlightTypeEnum.values());
        modelMap.put("websiteNames", websiteService.getAllWebsiteNames());
        modelMap.put("flightForm", flightService.findOneForm(flightId));
        return CREATE_FLIGHT_MAIN_VIEW;
    }

    @RequestMapping(value = "/create/main", method = RequestMethod.POST)
    public String postCreateFlightMain(FlightForm flightForm, ModelMap modelMap) throws ParseException {
        if (checkIdEqualsZero(flightForm)) {
            return REDIRECT + CREATE_FLIGHT_START_PAGE;
        }
        try{
            flightService.updateFlight(flightForm);
        }catch (Exception e){
            modelMap.put("error", "Error:" + e.getMessage());
            return CREATE_FLIGHT_MAIN_VIEW;
        }
        return REDIRECT + CREATE_FLIGHT_FINISH_PAGE + FLIGHT_ID_PARAM + flightForm.getFlightId();
    }

    @RequestMapping(value = "/create/finish")
    public String getCreateFlightFinish(@RequestParam(value = "flightId", required = false) String flightId, ModelMap modelMap) throws ParseException {
        if (flightId==null) {
            return REDIRECT + CREATE_FLIGHT_START_PAGE;
        }
        modelMap.put("flightForm", flightService.findOneForm(flightId));
        return CREATE_FLIGHT_FINISH_VIEW;
    }

    @RequestMapping(value = "/create/finish", method = RequestMethod.POST)
    public String postCreateFlightFinish(FlightForm flightForm, ModelMap modelMap) throws ParseException {
        if (checkIdEqualsZero(flightForm)) {
            return REDIRECT + CREATE_FLIGHT_START_PAGE;
        }
        return REDIRECT + APP_PAGE;
    }

    @RequestMapping(value = "/{flightId}/edit")
    public String getEditFlight(@PathVariable(value = "flightId") String flightId, ModelMap modelMap) throws ParseException {
        modelMap.put("types", FlightTypeEnum.values());
        modelMap.put("flightId", flightId);
        modelMap.put("websiteNames", websiteService.getAllWebsiteNames());
        modelMap.put("campaignNames", campaignService.getAllCampaignsNames());
        modelMap.put("flightForm", flightService.findOneForm(flightId));
        return EDIT_FLIGHT_VIEW;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEditFlight(FlightForm flightForm, ModelMap modelMap) throws ParseException {
        try{
            flightService.updateFlight(flightForm);
        }catch (Exception e){
            modelMap.put("error", "Error:" + e.getMessage());
            return EDIT_FLIGHT_VIEW;
        }
        return REDIRECT + APP_PAGE;
    }

    private boolean checkIdEqualsZero(FlightForm flightForm) {
        return flightForm.getFlightId().equals(0L);
    }
}
