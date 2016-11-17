package com.cbinfo.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class CampaignCreationDTO {

    private String name;

    @NotEmpty(message = "Empty flightName!")
    private String flightName;

    private String flightStartDate;

    private String flightEndDate;

    private String flightWebsiteName;

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public String getFlightStartDate() {
        return flightStartDate;
    }

    public void setFlightStartDate(String flightStartDate) {
        this.flightStartDate = flightStartDate;
    }

    public String getFlightEndDate() {
        return flightEndDate;
    }

    public void setFlightEndDate(String flightEndDate) {
        this.flightEndDate = flightEndDate;
    }

    public String getFlightWebsiteName() {
        return flightWebsiteName;
    }

    public void setFlightWebsiteName(String flightWebsiteName) {
        this.flightWebsiteName = flightWebsiteName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
