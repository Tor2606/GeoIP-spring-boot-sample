package com.cbinfo.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "websites")
public class Website {

    // TODO: 22.11.2016 write manually join table

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "website_id")
    private long websiteId;

    @Column(name = "website_name")
    private String websiteName;

    @ManyToMany
    @JoinTable(
            name = "websites_flights",
            joinColumns = @JoinColumn(name = "website_id"),
            inverseJoinColumns = @JoinColumn(name = "flight_id"))
    private Set<Flight> flights;

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

    public Set<Flight> getFlights() {
        return flights;
    }

    public void setFlights(Set<Flight> flights) {
        this.flights = flights;
    }
}
