package com.cbinfo.dto.form;

import com.cbinfo.model.Website;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

public class FlightForm {
    @NotEmpty(message = "Empty name!")
    private String name;

    private Date starDate;

    private Date endDate;

    private Website website;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStarDate() {
        return starDate;
    }

    public void setStarDate(Date starDate) {
        this.starDate = starDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }
}
