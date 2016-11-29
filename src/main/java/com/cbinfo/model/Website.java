package com.cbinfo.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "websites")
public class Website {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "website_id")
    private long websiteId;

    @Column(name = "website_name")
    private String websiteName;

    @OneToMany
    private List<Flight> flights;

    public long getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(long websiteId) {
        this.websiteId = websiteId;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
