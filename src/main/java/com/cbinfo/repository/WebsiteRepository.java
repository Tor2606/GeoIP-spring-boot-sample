package com.cbinfo.repository;

import com.cbinfo.model.Website;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WebsiteRepository extends CrudRepository<Website, Long> {
    @Query(value = "SELECT * from websites where user_id = ?2 and website_name = ?1", nativeQuery = true)
    Website findOneByWebsiteNameAndUserId(String websiteName, Long userId);

    @Query(value = "SELECT website_name from websites where user_id = ?1", nativeQuery = true)
    List<String> findWebsiteNamesByUserId(Long userId);

    @Query(value = "SELECT * from websites where user_id = ?1", nativeQuery = true)
    List<Website> findWebsitesByUserId(Long userId);
}