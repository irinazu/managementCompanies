package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.RepairWork;
import com.micro.managementCompanies.models.Request;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestRepository extends CrudRepository<Request,Long> {

    @Override
    Optional<Request> findById(Long aLong);
}
