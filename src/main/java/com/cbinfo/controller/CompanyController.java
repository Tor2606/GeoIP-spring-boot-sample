package com.cbinfo.controller;

import com.cbinfo.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/companies")
public class CompanyController {

    private static final String REDIRECT = "redirect:";
    private static final String APP_PAGE = "/app";

    @Autowired
    protected CompanyService companyService;

    @RequestMapping(value = "/delete/{companyId}", method = RequestMethod.GET)
    public java.lang.String deleteCompany(@PathVariable String companyId) {
        companyService.delete(companyId);
        return REDIRECT + APP_PAGE;
    }
}
