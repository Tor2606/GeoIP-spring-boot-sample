package com.cbinfo.service;

import com.cbinfo.model.Company;
import com.cbinfo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> findAll(){
        return (List<Company>) companyRepository.findAll();
    }
}
