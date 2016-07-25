package com.cbinfo.model;

/**
 * Created by Igor on 29.06.2016.
 */
public class CountryBrowserData {
    private String countryCode;
    private String userAgent;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
