package com.cbinfo.repository;

import com.cbinfo.model.Flight;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlightRepository extends CrudRepository<Flight, Long> {
    @Query(value = "SELECT * from flights where campaign_id = ?1", nativeQuery = true)
    List<Flight> findByCampaignId(Long campaignId);
}
