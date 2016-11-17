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
        flight.setWebsites(newArrayList(websiteService.findOneByName(flightForm.getName())));
        flight.setCampaign(campaignService.findOne(campaignId));
        flight.setFlightName(flightForm.getName());
        flight.setStartDate(toDate(flightForm.getStartDate()));
        flight.setEndDate(toDate(flightForm.getEndDate()));
        return flight;
    }
}
