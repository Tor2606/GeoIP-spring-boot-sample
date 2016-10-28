package com.cbinfo.service;

import com.cbinfo.model.Company;
import com.cbinfo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    public List<Company> findAll() {
        return (List<Company>) companyRepository.findAll();
    }

    public void createNewCompany(String newCompanyName) {
        Company newCompany = new Company();
        newCompany.setName(newCompanyName);
        this.save(newCompany);
    }

    @Transactional
    private void save(Company company) {
        companyRepository.save(company);
    }

    public Company getCompanyByName(String companyName) {
        return companyRepository.findOneByName(companyName);
    }

    public void delete(String companyId) {
        delete(Long.valueOf(companyId));
    }

    @Transactional
    public void delete(Long companyId){
        companyRepository.delete(companyId);
    }
}
