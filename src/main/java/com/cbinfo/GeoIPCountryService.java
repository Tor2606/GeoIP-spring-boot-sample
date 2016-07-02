package com.cbinfo;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;

/**
 * Created by Igor on 29.06.2016.
 */

@Component
public class GeoIPCountryService implements CountryService{

    @Override
    public String getCountry(String ip) {

        File file = new File("src/main/resources/location/GeoLiteCity.dat");
        LookupService lookup = null;
        try {
            lookup = new LookupService(file,LookupService.GEOIP_MEMORY_CACHE);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        Location locationServices = lookup.getLocation(ip);

        return locationServices.countryCode;
    }
}
