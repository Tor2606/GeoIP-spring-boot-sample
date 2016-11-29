package com.cbinfo.service;

import com.cbinfo.dto.form.FlightForm;
import com.cbinfo.model.Flight;
import com.cbinfo.model.enums.FlightTypes;
import com.cbinfo.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

import static com.cbinfo.utils.myDateUtil.dateToString;
import static com.cbinfo.utils.myDateUtil.toDate;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class FlightService {

    @Autowired
    protected CampaignService campaignService;

    @Autowired
    protected FlightRepository flightRepository;

    @Autowired
    protected WebsiteService websiteService;

    public long createFlight(FlightForm flightForm) throws ParseException {
        Flight flight = flightFormToFlight(flightForm);
        flight.setFlightName(constructFlightName(flight));
        saveFlight(flight);
        flight.setFlightName(constructFlightName(flight));
        flight = saveFlight(flight);
        return flight.getFlightId();
    }

    private Flight flightFormToFlight(FlightForm flightForm) throws ParseException {
        Flight result = new Flight();
        fillFlightFromDTO(flightForm, result);
        return result;
    }

    @Transactional
    private Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public FlightForm findFlightForm(String flightId) {
        return flightToFlightForm(findFlight(flightId));
    }

    @Transactional
    private Flight findFlight(String flightId) {
        return flightRepository.findOne(Long.valueOf(flightId));
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
            flight.setWebsite(websiteService.findWebsiteByName(flightForm.getWebsiteName()));
        }
        if (isNotBlank(flightForm.getCampaignName())) {
            flight.setCampaign(campaignService.findCampaignByName(flightForm.getCampaignName()));
        }
        if (isNotBlank(flightForm.getType())) {
            flight.setFlightType(FlightTypes.valueOf(flightForm.getType()));
        }
        if (flightForm.getQuantity()!= 0) {
            flight.setQuantity(flightForm.getQuantity());
        }
    }

    private String constructFlightName(Flight flight) {
        StringBuilder resultBuilder = new StringBuilder("flight_");
        if (flight.getFlightId() != 0L) resultBuilder.append(flight.getFlightId()).append("_");
        resultBuilder.append(flight.getCampaign().getCampaignName());
        if (isNotNull(flight.getStartDate())) resultBuilder.append("_").append(dateToString(flight.getStartDate()));
        if (isNotNull(flight.getEndDate())) resultBuilder.append("_").append(dateToString(flight.getEndDate()));
        if (isNotNull(flight.getWebsite())) resultBuilder.append("_").append(flight.getWebsite().getWebsiteName());
        if (isNotNull(flight.getFlightType())) resultBuilder.append("_").append(flight.getFlightType().name());
        return resultBuilder.toString();
    }

    private FlightForm flightToFlightForm(Flight flight) {
        FlightForm result = new FlightForm();
        result.setFlightId(String.valueOf(flight.getFlightId()));
        result.setCampaignName(flight.getCampaign().getCampaignName());
        result.setName(flight.getFlightName());
        if (isNotNull(flight.getStartDate())) result.setStartDate(dateToString(flight.getStartDate()));
        if (isNotNull(flight.getEndDate())) result.setEndDate(dateToString(flight.getEndDate()));
        if (isNotNull(flight.getWebsite())) result.setWebsiteName(flight.getWebsite().getWebsiteName());
        if (isNotNull(flight.getFlightType())) result.setType(flight.getFlightType().name());
        if (isNotNull(flight.getQuantity())) result.setQuantity(flight.getQuantity());
        return result;
    }

    private <T> boolean isNotNull(T object) {
        return object != null;
    }

    public void updateFlight(FlightForm flightForm) throws ParseException {
        Flight flight = findFlight(flightForm.getFlightId());
        fillFlightFromDTO(flightForm, flight);
        flight.setFlightName(constructFlightName(flight));
        saveFlight(flight);
    }

    public List<FlightForm> findFlightFormsByCampaign(String campaignId) {
        List<Flight> flights = findFlightsByCampaignId(Long.valueOf(campaignId));
        return flights.stream().map(this::flightToFlightForm).collect(toList());
    }

    @Transactional
    private List<Flight> findFlightsByCampaignId(Long campaignId) {
        return flightRepository.findByCampaignId(campaignId);
    }

    @Transactional
    public void deleteFlight(String flightId) {
        flightRepository.delete(Long.valueOf(flightId));
    }
}
