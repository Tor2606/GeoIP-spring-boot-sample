package com.cbinfo.controller;

import com.cbinfo.model.Banner;
import com.cbinfo.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/ad")
public class AdController {

    // TODO: 20.01.2017 add banners by flight id, it will be like in businessclick

    private static final String AD_TEMPLATE_VIEW = "adTemplate";

    @Autowired
    private BannerService bannerService;

    @RequestMapping(value = "/script/{flightId}")
    public String getBanner(@PathVariable String flightId, ModelMap modelMap) {
        List<Banner> banners = bannerService.findBannersByFLightId(flightId);
        modelMap.put("banners", banners);
        return AD_TEMPLATE_VIEW;
    }
}
