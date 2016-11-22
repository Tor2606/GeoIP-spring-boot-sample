package com.cbinfo.dto.form;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

public class FlightForm {

    private Long flightId;

    @NotEmpty(message = "Empty name!")
    private String name;

    private String startDate;

    private String endDate;

    private List<String> websiteNames;

    private String campaignName;

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<String> getWebsiteNames() {
        return websiteNames;
    }

    public void setWebsiteNames(List<String> websiteNames) {
        this.websiteNames = websiteNames;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }
}
