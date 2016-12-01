package com.cbinfo.service;

import com.cbinfo.dto.CampaignDTO;
import com.cbinfo.model.Campaign;
import com.cbinfo.model.User;
import com.cbinfo.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;


@Service
public class CampaignService {
    private static final String USER_ROLE = "ROLE_USER";
    private static final String CAMPAIGN_CREATION_TIME_TEMPLATE = "dd/MM/yyyy HH:mm:ss";

    @Autowired
    protected UserSessionService userSessionService;

    @Autowired
    protected CampaignRepository campaignRepository;

    @Autowired
    protected WebsiteService websiteService;

    @Autowired
    protected FlightService flightService;

    private void checkIfNotExist(String campaignName) throws Exception {
        checkArgument(campaignRepository.findOneByCampaignName(campaignName) == null, "Campaign with such name all ready exists!");
    }

    @Transactional
    private Campaign saveCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    public List<Campaign> findAllCurrentUserCampaigns() {
        List<Campaign> result = findAll();
        User user = userSessionService.getUser();
        if (user.getRole().name().equals(USER_ROLE)) {
           result = result.stream().filter((campaign) -> campaign.getUser().equals(user)).collect(toList());
        }
        return result;
    }

    @Transactional
    private List<Campaign> findAll() {
        return newArrayList(campaignRepository.findAll());
    }

    @Transactional
    public void deleteCampaign(String campaignId) {
        campaignRepository.delete(Long.valueOf(campaignId));
    }

    public CampaignDTO findCampaignDTO(String campaignId) {
        return campaignToDTO(findCampaign(Long.valueOf(campaignId)));
    }

    private CampaignDTO campaignToDTO(Campaign campaign) {
        CampaignDTO result = new CampaignDTO();
        result.setCampaignId(String.valueOf(campaign.getCampaignId()));
        result.setCampaignName(campaign.getCampaignName());
        DateFormat dateFormat = new SimpleDateFormat(CAMPAIGN_CREATION_TIME_TEMPLATE);
        result.setCreated(dateFormat.format(campaign.getCreated()));
        return result;
    }

    @Transactional
    private Campaign findCampaign(Long campaignId) {
        return campaignRepository.findOne(campaignId);
    }

    public List<String> findAllCampaignsNames() {
        return findAllCurrentUserCampaigns().stream().map(Campaign::getCampaignName).collect(toList());
    }

    public void createCampaign(CampaignDTO campaignDTO) throws Exception {
        checkIfNotExist(campaignDTO.getCampaignName());
        Campaign campaign = new Campaign();
        campaign.setCreated(new Date());
        campaign.setCampaignName(campaignDTO.getCampaignName());
        campaign.setUser(userSessionService.getUser());
        saveCampaign(campaign);
    }

    public void updateCampaignName(CampaignDTO campaignDTO) throws Exception {
        checkIfNotExist(campaignDTO.getCampaignName());
        Campaign campaign = findCampaign(campaignDTO.getCampaignId());
        campaign.setCampaignName(campaignDTO.getCampaignName());
        saveCampaign(campaign);
    }

    public Campaign findCampaign(String campaignId) {
        return findCampaign(Long.valueOf(campaignId));
    }

    public Campaign findCampaignByName(String campaignName) {
        return campaignRepository.findOneByCampaignName(campaignName);
    }
}
