package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.RequestStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestStatusRepository extends CrudRepository<RequestStatus,Long> {

    @Override
    Iterable<RequestStatus> findAll();
}
