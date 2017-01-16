package com.cbinfo.controller;

import com.cbinfo.model.Banner;
import com.cbinfo.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/ad")
public class AdController {

    private static final String AD_TEMPLATE_VIEW = "adTemplate";
    @Autowired
    private BannerService bannerService;

    @RequestMapping(value = "/script/{bannerId}")
    public String getBanner(@PathVariable String bannerId, ModelMap modelMap) {
        Banner banner = bannerService.findBanner(bannerId);
        modelMap.put("link", banner.getUrl());
        modelMap.put("title", banner.getTitle());
        return AD_TEMPLATE_VIEW;
    }
}
