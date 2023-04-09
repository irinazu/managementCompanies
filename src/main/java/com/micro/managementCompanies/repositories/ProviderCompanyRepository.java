package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.ProviderCompany;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderCompanyRepository extends CrudRepository<ProviderCompany,Long> {


}
