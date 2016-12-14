package com.cbinfo.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "campaigns")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id")
    private long campaignId;

    @Column(name = "campaign_name")
    private String campaignName;

    @Column
    private Date created;

    @OneToMany(mappedBy = "campaign")
    private List<Flight> flights;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String name) {
        this.campaignName = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
