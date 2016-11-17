package com.cbinfo.repository;

import com.cbinfo.model.Website;
import org.springframework.data.repository.CrudRepository;

public interface WebsiteRepository extends CrudRepository<Website, Long> {
    Website findOneByWebsiteName(String websiteName);
}
