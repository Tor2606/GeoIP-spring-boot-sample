package com.cbinfo.service;

import com.cbinfo.dto.form.WebsiteForm;
import com.cbinfo.model.Website;
import com.cbinfo.repository.WebsiteRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class WebsiteService {
    @Autowired
    private WebsiteRepository websiteRepository;

    @Transactional
    public List<Website> findAll() {
        return Lists.newArrayList(websiteRepository.findAll());
    }

    public List<String> findAllNames() {
        return findAll().stream().map(Website::getWebsiteName).collect(toList());
    }

    @Transactional
    public Website findOneByName(String websiteName) {
        return websiteRepository.findOneByWebsiteName(websiteName);
    }


    public Website findOne(String websiteId) {
        return findOne(Long.valueOf(websiteId));
    }

    @Transactional
    private Website findOne(Long websiteId) {
        return websiteRepository.findOne(websiteId);
    }

    public void editWebsite(String websiteId, WebsiteForm websiteForm) throws Exception {
        if (isBlank(websiteForm.getName())) {
            throw new Exception("Name can't be empty");
        }
        if (checkIfNameAlreadyExist(websiteForm.getName())) {
            throw new Exception("Such site all-ready exist!");
        }
        Website website = websiteRepository.findOne(Long.valueOf(websiteId));
        fillWebsite(website,websiteForm);
        saveWebsite(website);
    }

    @Transactional
    private void saveWebsite(Website website) {
        websiteRepository.save(website);
    }

    private void fillWebsite(Website website, WebsiteForm websiteForm) {
        website.setWebsiteName(websiteForm.getName());
    }

    private boolean checkIfNameAlreadyExist(String name) {
        return websiteRepository.findOneByWebsiteName(name) == null ? false : true;
    }

    public WebsiteForm findOneForm(String websiteId) {
        Website website = findOne(websiteId);
        return toForm(website);

    }

    private WebsiteForm toForm(Website website) {
        WebsiteForm result = new WebsiteForm();
        result.setName(website.getWebsiteName());
        return result;
    }

    public void createWebsite(WebsiteForm websiteForm) throws Exception {
        if(checkIfNameAlreadyExist(websiteForm.getName())){
            throw new Exception("Website all-ready exist!");
        }
        Website website = new Website();
        fillWebsite(website, websiteForm);
        saveWebsite(website);
    }

    public void deleteWebsite(String websiteId) {
        deleteWebsite(Long.valueOf(websiteId));
    }

    @Transactional
    private void deleteWebsite(Long websiteId) {
        websiteRepository.delete(websiteId);
    }
}