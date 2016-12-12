package com.cbinfo.dto.form;

import org.springframework.web.multipart.MultipartFile;

public class BannerForm {

    private String id;

    private String flightId;

    private String title;

    private String description;

    private String url;

    private MultipartFile file;

    public BannerForm(String flightId, String title, String description, String url, MultipartFile file) {
        this.flightId = flightId;
        this.title = title;
        this.description = description;
        this.url = url;
        this.file = file;
    }

    public BannerForm(){}

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
