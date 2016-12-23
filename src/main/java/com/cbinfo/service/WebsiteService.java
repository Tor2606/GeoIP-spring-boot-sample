package com.cbinfo.service;

import com.cbinfo.model.Website;
import com.cbinfo.repository.WebsiteRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class WebsiteService {
    private static final String ADMIN = "ROLE_ADMIN";
    @Autowired
    private WebsiteRepository websiteRepository;

    @Autowired
    private UserSessionService userSessionService;

    @Transactional
    public List<Website> findAll() {
        return Lists.newArrayList(websiteRepository.findAll());
    }

    public List<Website> findAllWebsitesForCurrentUser() {
        if (userSessionService.getUser().getRole().equals(ADMIN)){
            return findAll();
        }
        return findAllWebsitesByUserId(userSessionService.getUser().getUserId());
    }

    @Transactional
    private List<Website> findAllWebsitesByUserId(Long userId) {
        return websiteRepository.findWebsitesByUserId(userId);
    }

    private List<String> findAllWebsiteNames() {
        return findAll().stream().map(Website::getWebsiteName).collect(toList());
    }

    @Transactional
    protected Website findWebsiteByNameForCurrentUser(String websiteName) {
        return websiteRepository.findOneByWebsiteNameAndUserId(websiteName, userSessionService.getUser().getUserId());
    }

    @Transactional
    private Website findWebsite(String websiteId) {
        return websiteRepository.findOne(Long.valueOf(websiteId));
    }

    public void editWebsite(String websiteId, String name) throws Exception {
        if (isBlank(name)) {
            throw new Exception("Name can't be empty");
        }
        if (checkIfNameAlreadyExist(name)) {
            throw new Exception("Such site all-ready exist!");
        }
        Website website = websiteRepository.findOne(Long.valueOf(websiteId));
        fillWebsite(website, name);
        saveWebsite(website);
    }

    @Transactional
    private void saveWebsite(Website website) {
        websiteRepository.save(website);
    }

    private void fillWebsite(Website website, String name) {
        website.setWebsiteName(name);
        website.setUser(userSessionService.getUser());
    }

    private boolean checkIfNameAlreadyExist(String name) {
        return findWebsiteByNameForCurrentUser(name) != null;
    }

    public String findWebsiteNameById(String websiteId) {
        return findWebsite(websiteId).getWebsiteName();
    }

    @Transactional
    public void deleteWebsite(String websiteId) {
        websiteRepository.delete(Long.valueOf(websiteId));
    }

    public void createWebsite(String newWebsiteName) throws Exception {
        checkArgument(findWebsiteByNameForCurrentUser(newWebsiteName) == null, "Such website already exists!");
        Website website = new Website();
        fillWebsite(website, newWebsiteName);
        saveWebsite(website);
    }

    @Transactional
    private List<String> findWebsiteNamesByUserId(long userId) {
        return websiteRepository.findWebsiteNamesByUserId(userId);
    }

    public List<String> findAllWebsiteNamesForCurrentUser() {
        if(userSessionService.getUser().getRole().equals(ADMIN)){
            return findAllWebsiteNames();
        }
        return findWebsiteNamesByUserId(userSessionService.getUser().getUserId());
    }
}
