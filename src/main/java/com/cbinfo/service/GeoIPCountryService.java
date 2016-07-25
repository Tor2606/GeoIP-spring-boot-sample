package com.cbinfo.service;

import com.cbinfo.model.CountryBrowserData;
import com.cbinfo.DTO.IPInfoDTO;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

/**
 * Created by Igor on 29.06.2016.
 */

@Component
public class GeoIPCountryService {
    public static final String SERVICE_URL_ADDRESS = "http://www.ipinfo.io/";
    final Logger logger = LoggerFactory.getLogger(GeoIPCountryService.class);

    LoadingCache<String, String> countryCache =
            CacheBuilder.newBuilder()
                    .maximumSize(100)
                    .expireAfterAccess(10, TimeUnit.MINUTES)
                    .build(new CacheLoader<String, String>() {

                        @Override
                        public String load(String ip) throws IOException {
                            return getCountry(ip);
                        }
                    });


    public String getCountryFromDatabase(String ip) throws IOException {

        File file = new File("src/main/resources/location/GeoLiteCity.dat");
        LookupService lookup = null;
        lookup = new LookupService(file,LookupService.GEOIP_MEMORY_CACHE);
        Location locationServices = lookup.getLocation(ip);
        return locationServices.countryCode;
    }

    public CountryBrowserData getData(String ip, String userAgent){
        CountryBrowserData countryBrowserData = new CountryBrowserData();
        countryBrowserData.setUserAgent(userAgent);
        try {
            countryBrowserData.setCountryCode(countryCache.get(ip));
        } catch (ExecutionException e) {
            logger.error(e.getMessage());
        }
        return countryBrowserData;
    }

    private String getCountry(String ip) throws IOException {

            String URLAdress = SERVICE_URL_ADDRESS + ip + "/json";
            String data = this.readDataFromURL(URLAdress);
            Gson gson = new GsonBuilder().create();
            IPInfoDTO ipInfoDTO= gson.fromJson(data, IPInfoDTO.class);
            return ipInfoDTO.getCountry();
    }

    private String readDataFromURL(String url) throws IOException {

        URL ipInfoURL = new URL(url);
        URLConnection urlConnection = ipInfoURL.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String input;
        StringBuilder result = new StringBuilder();
        while ((input = bufferedReader.readLine()) != null) {
            result.append(input);
        }
        bufferedReader.close();
        return result.toString();
    }

}

/*

CountryBrowserData data = new CountryBrowserData();

if(request.getHeader("User-Agent")!= null ) {
        String browserInfo = request.getHeader("User-Agent");
        data.setUserAgent(browserInfo);
        }else{
        data.setUserAgent("undefined");
        }

        String countryInfo = this.countryService.getCountry(ip);
        data.setCountryCode(countryInfo);*/
