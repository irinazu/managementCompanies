package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.ManagementCompany;
import com.micro.managementCompanies.models.RepairWork;
import com.micro.managementCompanies.models.Request;
import com.micro.managementCompanies.models.RequestUpdate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends CrudRepository<Request,Long> {

    @Override
    Optional<Request> findById(Long aLong);

    List<Request> findAllByManagementCompanyRequestId(Long managementCompany);
}
