package com.cbinfo.service;

import com.cbinfo.dto.CampaignCreationDTO;
import com.cbinfo.dto.form.FlightForm;
import com.cbinfo.model.Campaign;
import com.cbinfo.model.Flight;
import com.cbinfo.model.Website;
import com.cbinfo.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.cbinfo.utils.myDateUtil.toDate;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class FlightService {

    private static final String DATE_FORMAT_TEMPLATE = "dd/MM/yy";

    @Autowired
    protected CampaignService campaignService;

    @Autowired
    protected FlightRepository flightRepository;

    @Autowired
    protected WebsiteService websiteService;

    public void createAndSaveFlight(CampaignCreationDTO creationDTO, long campaignId) throws ParseException {
        createAndSaveFlight(toFlightForm(creationDTO), campaignId);
    }

    public long createFlight(FlightForm flightForm) throws ParseException {
        long flightId = saveFlightForm(flightForm);
        return flightId;
    }

    private long saveFlightForm(FlightForm flightForm) {
        return 0;
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
        result.setWebsiteNames(newArrayList());
        result.getWebsiteNames().add(form.getFlightWebsiteName());
        return result;
    }

    private Flight toFlight(FlightForm flightForm, long campaignId) throws ParseException {
        Flight flight = new Flight();
        flight.setWebsites(newArrayList());
        flight.setCampaign(campaignService.findOne(campaignId));
        fillFlightFromDTO(flightForm, flight);
        return flight;
    }

    public FlightForm findOneForm(String flightId){
        return toFlightForm(findOne(flightId));
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
        List<Website> flightWebsiteList = flight.getWebsites();
        flightForm.getWebsiteNames().stream().filter(wn -> checkIfWebsiteIsNotPresent(wn, flightWebsiteList)).forEach(name ->flightWebsiteList.add(websiteService.findOneByName(name)));
    }

    private boolean checkIfWebsiteIsNotPresent(String websiteName, List<Website> flightWebsiteList) {
        List<String> websiteNameList = flightWebsiteList.stream().map(Website::getWebsiteName).collect(toList());
        return !websiteNameList.contains(websiteName);
    }

    public List<FlightForm> getCampaignFLightsToDTO(Campaign campaign) {
        List<FlightForm> result = newArrayList();
        campaign.getFlights().stream().forEach(f->result.add(toFlightForm(f)));
        return  result;
    }

    private FlightForm toFlightForm(Flight flight) {
        FlightForm result = new FlightForm();
        result.setFlightId(flight.getFlightId());
        result.setName(flight.getFlightName());
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_TEMPLATE);
        result.setStartDate(dateFormat.format(flight.getStartDate()));
        result.setEndDate(dateFormat.format(flight.getEndDate()));
        result.setWebsiteNames(newArrayList());
        flight.getWebsites().stream().map(Website::getWebsiteName).forEach(name -> result.getWebsiteNames().add(name));
        return result;
    }

    public void updateFlight(FlightForm flightForm) {

    }

    public List<FlightForm> findFlightFormsByCampaign(String campaignId) {
        return null;
    }
}
