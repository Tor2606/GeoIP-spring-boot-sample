package com.cbinfo.dto;

import java.util.List;

public class CampaignDTO {

    private String campaignName;

    private String created;

    private List<String> flightNames;

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<String> getFlightNames() {
        return flightNames;
    }

    public void setFlightNames(List<String> flightNames) {
        this.flightNames = flightNames;
    }
}
