package com.cbinfo.model;


// TODO: 07.11.2016 Create new campaign, it's creation and edit page

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "campaigns")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id")
    private long campaignId;

    @Column
    private String name;

    @Column
    private Date created;

    @OneToMany(mappedBy = "campaign")
    private Set<Flight> flights;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<Flight> getFlights() {
        return flights;
    }

    public void setFlights(Set<Flight> flights) {
        this.flights = flights;
    }
}
