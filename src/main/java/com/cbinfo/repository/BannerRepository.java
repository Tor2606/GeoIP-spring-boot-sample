package com.cbinfo.repository;

import com.cbinfo.model.Banner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BannerRepository extends CrudRepository<Banner, Long> {
    Banner findOneByTitle(String title);

    @Query(value = "SELECT * from banners where flight_id = ?1", nativeQuery = true)
    Iterable<Banner> findBannersByFlightId(long flightId);
}
