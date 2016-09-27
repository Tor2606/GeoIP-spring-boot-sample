package com.cbinfo.dto;

/**
 * Created by Igor on 22.07.2016.
 */
public class UserDataDTO {

    private String ip;
    private String country;
    private String userAgent;
    private String browser;
    private String operatingSystem;
    private String agentFamily;
    private String producer;

    public String getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(String deviceCategory) {
        this.deviceCategory = deviceCategory;
    }

    private String deviceCategory;

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public void setAgentFamily(String agentFamily) {
        this.agentFamily = agentFamily;
    }

    public String getAgentFamily() {
        return agentFamily;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getProducer() {
        return producer;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
