package com.cbinfo.repository;

import com.cbinfo.model.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Long> {
    Company findOneByName(String companyName);
}
