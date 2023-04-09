package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Request;
import com.micro.managementCompanies.models.RequestUpdate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RequestUpdateRepository extends CrudRepository<RequestUpdate,Long> {

    @Query("select ru from RequestUpdate ru where ru.request.id=:idRequest")
    List<RequestUpdate> findByRequestId(Long idRequest);
}
