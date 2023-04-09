package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.ServiceDescription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceDescriptionRepository extends CrudRepository<ServiceDescription,Long> {

    @Override
    Iterable<ServiceDescription> findAll();

    @Override
    Optional<ServiceDescription> findById(Long aLong);
}
