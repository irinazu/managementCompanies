package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Service;
import com.micro.managementCompanies.models.ServiceDescription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends CrudRepository<Service,Long> {
    @Query("select s from Service s where s.serviceDescription.id=:service order by s.dateOfConsumption")
    public List<Service> getDataForService(Long service);

    public List<Service> findFirst12ByServiceDescriptionOrderByDateOfConsumption(ServiceDescription service);

    @Query("select s from Service s where s.serviceDescription.id=:service and s.repaid=:repaid order by s.dateOfConsumption desc")
    public List<Service> getDebtsForService(Long service,Boolean repaid);

    @Query("select s from Service s where s.year=:year and s.monthNumber=:month")
    public List<Service> getAllServicesForDate(Integer year,Integer month);

    @Query("select distinct s.year from Service s")
    List<Integer> getYears();
}
