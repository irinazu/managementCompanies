package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.ManagementCompany;
import com.micro.managementCompanies.models.ManagementCompanyStatus;
import org.springframework.data.repository.CrudRepository;

public interface ManagementCompanyStatusRepository extends CrudRepository<ManagementCompanyStatus,Long> {
}
