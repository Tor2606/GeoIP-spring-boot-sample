package com.cbinfo.service;

import com.cbinfo.dto.CampaignCreationDTO;
import com.cbinfo.dto.CampaignDTO;
import com.cbinfo.dto.form.FlightForm;
import com.cbinfo.model.Campaign;
import com.cbinfo.model.Flight;
import com.cbinfo.model.User;
import com.cbinfo.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


@Service
public class CampaignService {

    private static final String USER_ROLE = "ROLE_USER";

    @Autowired
    protected UserSessionService userSessionService;

    @Autowired
    protected CampaignRepository campaignRepository;

    @Autowired
    protected WebsiteService websiteService;

    @Autowired
    protected FlightService flightService;

    public void checkIfNotExist(String campaignName) throws Exception {
        if (campaignRepository.findOneByCampaignName(campaignName) != null)
            throw new Exception("Campaign with such name all ready exists!");
    }

    public Campaign saveCampaignCreationDTO(CampaignCreationDTO campaignCreationDTO) throws ParseException {
        Campaign campaign = toCampaign(campaignCreationDTO);
        campaign = saveCampaign(campaign);
        flightService.createAndSaveFlight(campaignCreationDTO, campaign.getCampaignId());
        return campaign;
    }

    private Campaign toCampaign(CampaignCreationDTO campaignCreationDTO) throws ParseException {
        Campaign campaign = new Campaign();
        campaign.setUser(userSessionService.getUser());
        campaign.setCampaignName(campaignCreationDTO.getName());
        campaign.setCreated(new Date());
        return campaign;
    }


    @Transactional
    public Campaign saveCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
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

    public CampaignDTO findOneDTO(String campaignId) {
        return toDTO(findOne(Long.valueOf(campaignId)));
    }

    private CampaignDTO toDTO(Campaign campaign) {
        CampaignDTO result = new CampaignDTO();
        result.setCampaignName(campaign.getCampaignName());
        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
        result.setCreated(dateFormat.format(campaign.getCreated()));
        result.setFlightNames(campaign.getFlights().stream().map(Flight::getFlightName).collect(toList()));
        return result;
    }

    @Transactional
    public Campaign findOne(Long campaignId) {
        return campaignRepository.findOne(campaignId);
    }

    public void createFlight(FlightForm flightForm, String campaignId) {

    }

    public Campaign findOneByName(String campaignName) {
        return campaignRepository.findOneByCampaignName(campaignName);
    }

    public void updateCampaignName(String campaignId, CampaignDTO campaignDTO) throws Exception {
        Campaign campaign = findOneByName(campaignId);
        if (isNotBlank(campaignDTO.getCampaignName())) {
            if (checkIfCampaignWithSuchNameExist(campaignDTO)) {
                throw new Exception("Campaign with such name allready exist!");
            }
            campaign.setCampaignName(campaignDTO.getCampaignName());
            campaignRepository.save(campaign);
        }
    }

    private boolean checkIfCampaignWithSuchNameExist(CampaignDTO campaignDTO) {
        return campaignRepository.findOneByCampaignName(campaignDTO.getCampaignName()) != null;
    }
}
