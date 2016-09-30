package com.cbinfo.model;

import com.cbinfo.dto.UserDataDTO;
import com.cbinfo.service.UserService;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by islabukhin on 19.09.16.
 */
@Entity
@Table(name = "user_data")
public class UserData {

    @Id
    @Column(name = "user_data_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_data_id;

    @Column(name = "time")
    @Temporal(TemporalType.DATE)
    private Date time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "ip")
    private String ip;

    @Column(name = "country")
    private String country;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "browser")
    private String browser;

    @Column(name = "operating_ystem")
    private String operatingSystem;

    @Column(name = "agent_family")
    private String agentFamily;

    @Column(name = "producer")
    private String producer;

    @Column(name ="device_category")
    private String deviceCategory;

    public String getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(String deviceCategory) {
        this.deviceCategory = deviceCategory;
    }

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

    public long getUser_data_id() {
        return user_data_id;
    }

    public void setUser_data_id(long user_data_id) {
        this.user_data_id = user_data_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
