package com.cbinfo;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Igor on 29.06.2016.
 */

@Controller
public class MainController {

    private CountryService countryService;


    @RequestMapping(value="/test", method=RequestMethod.GET)
    public String getSite(Map<String, Object> model) {
        return "test2";
    }

    @ResponseBody
    @RequestMapping(value="/test/ajax")
    public CBData getAjaxCBData(@RequestParam(name = "ip") String ip, HttpServletRequest request) {
        CBData data = new CBData();

        if(request.getHeader("User-Agent")!= null ) {
            String browserInfo = request.getHeader("User-Agent");
            data.setBrowserInfo(browserInfo);
        }else{
            data.setBrowserInfo("undefined");
        }

        String countryInfo = this.countryService.getCountry(ip);
        data.setCountryCode(countryInfo);
        System.out.println(countryInfo);
        return data;
    }


    @ResponseBody
    @RequestMapping(value="/info", method=RequestMethod.GET)
    public CBData getCBData(HttpServletRequest request) {
        CBData data = new CBData();

        if(request.getHeader("User-Agent")!= null ) {
            String browserInfo = request.getHeader("User-Agent");
            data.setBrowserInfo(browserInfo);
        }else{
            data.setBrowserInfo("undefined");
        }

        String ip = request.getRemoteAddr();
        String countryInfo = this.countryService.getCountry(ip);
        data.setCountryCode(countryInfo);
        return data;
    }


    @Autowired
    public void setCountryServiceService(CountryService countryService){
        this.countryService = countryService;
    }
}
