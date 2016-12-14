package com.cbinfo.controller;

import com.cbinfo.dto.form.BannerForm;
import com.cbinfo.model.User;
import com.cbinfo.service.BannerService;
import com.cbinfo.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/app/flights")
public class BannerController {
    private static final String REDIRECT = "redirect:";
    private static final String FLIGHTS = "/app/flights/";
    private static final String BANNERS = "/banners";

    private static final String BANNER_LIST_VIEW = "campaigns/flights/banners/bannerList";

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private BannerService bannerService;

    @ModelAttribute("user")
    public User user() {
        return userSessionService.getUser();
    }

    @RequestMapping(value = "/banners/create", method = RequestMethod.POST)
    public String postCreateBanner(BannerForm bannerForm, ModelMap modelMap) {
        try {
            bannerService.createBanner(bannerForm);
        } catch (Exception e) {
            modelMap.put("error", "Error: " + e.getMessage());
            modelMap.put("flightId", bannerForm.getFlightId());
            modelMap.put("banners", bannerService.findBannerFormByFlight(bannerForm.getFlightId()));
            return BANNER_LIST_VIEW;
        }
        return REDIRECT + FLIGHTS + bannerForm.getFlightId() + BANNERS;
    }

    @RequestMapping(value = "/{flightId}/banners/edit", method = RequestMethod.POST)
    public String postEditBanner(@PathVariable String flightId, BannerForm bannerForm, ModelMap modelMap) {
        try {
            bannerService.editBanner(bannerForm);
        } catch (Exception e) {
            modelMap.put("error", "Error: " + e.getMessage());
            modelMap.put("flightId", flightId);
            modelMap.put("banners", bannerService.findBannerFormByFlight(bannerForm.getFlightId()));
            return BANNER_LIST_VIEW;
        }
        return REDIRECT + FLIGHTS + flightId + BANNERS;
    }

    @RequestMapping(value = "/{flightId}/banners")
    public String getBannerList(@PathVariable String flightId, ModelMap modelMap) {
        modelMap.put("flightId", flightId);
        modelMap.put("banners", bannerService.findBannerFormByFlight(flightId));
        return BANNER_LIST_VIEW;
    }

    @RequestMapping(value = "/{flightId}/banners/{bannerId}/delete")
    public String deleteBanner(@PathVariable String flightId, @PathVariable String bannerId, ModelMap modelMap) {
        try {
            bannerService.delete(bannerId);
        } catch (Exception e) {
            modelMap.put("error", "Error: " + e.getMessage());
            modelMap.put("flightId", flightId);
            modelMap.put("banners", bannerService.findBannerFormByFlight(flightId));
            return BANNER_LIST_VIEW;
        }
        return REDIRECT + FLIGHTS + flightId + BANNERS;
    }

    @ResponseBody
    @RequestMapping(value = "/banners/img")
    public byte[] getBannerImage(@RequestParam(name = "title") String title){
        return bannerService.getBannerImage(title);
    }
}