package com.cbinfo.service;

import com.cbinfo.dto.form.FlightForm;
import com.cbinfo.model.Campaign;
import com.cbinfo.model.User;
import com.cbinfo.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;


@Service
public class CampaignService {

    private static final String USER_ROLE = "ROLE_USER";
    @Autowired
    protected UserSessionService userSessionService;

    @Autowired
    protected CampaignRepository campaignRepository;

    public Campaign createCampaign(String campaignName) throws Exception {
        checkIfNotExist(campaignName);
        Campaign campaign = new Campaign();
        setCampaignFieldsOnCreation(campaign, campaignName);
        return saveCampaign(campaign);
    }

    private void checkIfNotExist(String campaignName) throws Exception {
        if (campaignRepository.findOneByName(campaignName) != null)
            throw new Exception("Campaign with such name all ready exists!");
    }

    @Transactional
    private Campaign saveCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    private void setCampaignFieldsOnCreation(Campaign campaign, String campaignName) {
        campaign.setName(campaignName);
        campaign.setCreated(new Date());
        campaign.setUser(userSessionService.getUser());
    }

    public List<Campaign> findAllAvailableCampaigns() {
        List<Campaign> result = findAll();
        User user = userSessionService.getUser();
        if (user.getRole().name().equals(USER_ROLE)) {
            result.stream().filter((campaign) -> campaign.getUser().equals(user));
        }
        return result;
    }

    @Transactional
    public List<Campaign> findAll() {
        return newArrayList(campaignRepository.findAll());
    }

    public void deleteCampaign(String campaignId) {
        deleteCampaign(Long.valueOf(campaignId));
    }

    @Transactional
    private void deleteCampaign(Long campaignId) {
        campaignRepository.delete(campaignId);
    }

    public Campaign findOne(String campaignId) {
        return findOne(Long.valueOf(campaignId));
    }

    @Transactional
    private Campaign findOne(Long campaignId) {
        return campaignRepository.findOne(campaignId);
    }

    public void createFlight(FlightForm flightForm, String campaignId) {

    }
}
