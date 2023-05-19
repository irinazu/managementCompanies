package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.ManagementCompany;
import com.micro.managementCompanies.models.ManagementCompanyStatus;
import com.micro.managementCompanies.repositories.ManagementCompanyRepository;
import com.micro.managementCompanies.repositories.ManagementCompanyStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagementCompanyService {

    ManagementCompanyRepository managementCompanyRepository;
    ManagementCompanyStatusRepository managementCompanyStatusRepository;

    public ManagementCompanyService(ManagementCompanyRepository managementCompanyRepository, ManagementCompanyStatusRepository managementCompanyStatusRepository) {
        this.managementCompanyRepository = managementCompanyRepository;
        this.managementCompanyStatusRepository = managementCompanyStatusRepository;
    }

    //берем УК по id
    public ManagementCompany findManagementCompany(Long id){
        return managementCompanyRepository.findById(id).get();
    }

    //берем все УК
    public List<ManagementCompany> findAllManagementCompany(){
        return (List<ManagementCompany>) managementCompanyRepository.findAll();
    }

    //берем все УК  определенным статусом
    public List<ManagementCompany> findAllByManagementCompanyStatusId(Long status){
        return  managementCompanyRepository.findAllByManagementCompanyStatusId(status);
    }

    //берем все статусы УК
    public List<ManagementCompanyStatus> findAllStatus(){
        return (List<ManagementCompanyStatus>) managementCompanyStatusRepository.findAll();
    }

    //берем все статусы УК
    public ManagementCompanyStatus findByIdStatus(Long id){
        return managementCompanyStatusRepository.findById(id).get();
    }

    //берем все статусы УК
    public ManagementCompany saveManagementCompany(ManagementCompany managementCompany){
        return managementCompanyRepository.save(managementCompany);
    }
}
