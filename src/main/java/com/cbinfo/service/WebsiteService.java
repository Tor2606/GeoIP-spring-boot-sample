package com.cbinfo.service;

import com.cbinfo.model.Website;
import com.cbinfo.repository.WebsiteRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WebsiteService {
    @Autowired
    private WebsiteRepository websiteRepository;

    @Transactional
    public List<Website> findAll(){
        return Lists.newArrayList(websiteRepository.findAll());
    }

    @Transactional
    public Website findOneByName(String websiteName) {
        return websiteRepository.findOneByWebsiteName(websiteName);
    }

}
