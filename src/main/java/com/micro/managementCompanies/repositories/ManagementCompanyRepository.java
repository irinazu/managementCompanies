package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.ManagementCompany;
import com.micro.managementCompanies.models.News;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagementCompanyRepository extends CrudRepository<ManagementCompany,Long> {

    public List<ManagementCompany> findAllByManagementCompanyStatusId(Long status);

}
