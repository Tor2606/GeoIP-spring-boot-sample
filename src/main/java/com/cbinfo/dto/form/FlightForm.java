package com.cbinfo.dto.form;

public class FlightForm {

    private String flightId;

    private String name;

    private String startDate;

    private String endDate;

    private String websiteName;

    private String campaignName;

    private String newWebsiteName;

    private String type;

    private int quantity;

    public FlightForm() {
        this.flightId = "";
        this.campaignName = "";
        this.name = "";
        this.startDate = "";
        this.endDate = "";
        this.websiteName = "";
        this.type = "";
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
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

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNewWebsiteName() {
        return newWebsiteName;
    }

    public void setNewWebsiteName(String newWebsiteName) {
        this.newWebsiteName = newWebsiteName;
    }
}
