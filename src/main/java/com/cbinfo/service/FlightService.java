package com.cbinfo.service;

import com.cbinfo.dto.CampaignCreationDTO;
import com.cbinfo.dto.form.FlightForm;
import com.cbinfo.model.Flight;
import com.cbinfo.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

import static com.cbinfo.utils.myDateUtil.toDate;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class FlightService {

    @Autowired
    protected CampaignService campaignService;

    @Autowired
    protected FlightRepository flightRepository;

    @Autowired
    protected WebsiteService websiteService;

    public void createAndSaveFlight(CampaignCreationDTO creationDTO, long campaignId) throws ParseException {
        createAndSaveFlight(toFlightForm(creationDTO), campaignId);
    }

    public void createAndSaveFlight(FlightForm flightForm, String campaignId) throws ParseException {
        createAndSaveFlight(flightForm, Long.valueOf(campaignId));
    }

    @Transactional
    private void saveFlight(Flight flight) {
        flightRepository.save(flight);
    }

    private void createAndSaveFlight(FlightForm flightForm, long campaignId) throws ParseException {
        Flight flight = toFlight(flightForm, campaignId);
        saveFlight(flight);
    }

    private FlightForm toFlightForm(CampaignCreationDTO form) {
        FlightForm result = new FlightForm();
        result.setName(form.getName());
        result.setStartDate(form.getFlightStartDate());
        result.setEndDate(form.getFlightEndDate());
        result.setWebsiteName(form.getFlightWebsiteName());
        return result;
    }

    private Flight toFlight(FlightForm flightForm, long campaignId) throws ParseException {
        Flight flight = new Flight();
        flight.setWebsites(newArrayList());
        flight.setCampaign(campaignService.findOne(campaignId));
        fillFlightFromDTO(flightForm, flight);
        return flight;
    }

    public Flight findOne(String flightId) {
        return findOne(Long.valueOf(flightId));
    }

    @Transactional
    private Flight findOne(long flightId) {
        return flightRepository.findOne(flightId);
    }

    public void updateFlight(FlightForm flightForm, String flightId, String campaignId) throws ParseException {
        Flight flight = findOne(flightId);
        fillFlightFromDTO(flightForm, flight);
        saveFlight(flight);
    }

    private void fillFlightFromDTO(FlightForm flightForm, Flight flight) throws ParseException {
        if (isNotBlank(flightForm.getName())) {
            flight.setFlightName(flightForm.getName());
        }
        if (isNotBlank(flightForm.getStartDate())) {
            flight.setStartDate(toDate(flightForm.getStartDate()));
        }
        if (isNotBlank(flightForm.getEndDate())) {
            flight.setEndDate(toDate(flightForm.getEndDate()));
        }
        if (isNotBlank(flightForm.getWebsiteName())) {
            flight.getWebsites().add(websiteService.findOneByName(flightForm.getWebsiteName()));
        }
    }
}
