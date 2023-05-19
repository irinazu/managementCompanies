package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Service;
import com.micro.managementCompanies.models.ServiceDescription;
import com.micro.managementCompanies.models.UserSystem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends CrudRepository<Service,Long> {

    public List<Service> findFirst12ByServiceDescriptionIdAndUserIdOrderByDateOfConsumption(Long serviceDescriptionId,Long userId);

    @Query("select s from Service s where s.serviceDescription.id=:service and s.repaid=:repaid and s.user.id=:userId order by s.dateOfConsumption desc")
    public List<Service> getDebtsForService(Long service,Boolean repaid,Long userId);

    @Query("select distinct s.year from Service s")
    List<Integer> getYears();

    public List<Service> findAllByServiceDescriptionIdAndYearAndUserIdOrderByDateOfConsumption(Long serviceId,Integer year,Long userId);

    public Service findByServiceDescriptionIdAndYearAndMonthNumberAndUserId(Long serviceId,Integer year,Integer monthNumber,Long userId);

    public Optional<Service> findFirstByServiceDescriptionIdAndUserIdOrderByDateOfConsumptionDesc(Long serviceId,Long userID);

    public Optional<Service> findFirstByServiceDescriptionIdAndUserIdAndYearAndMonthNumberOrderByDateOfConsumptionDesc(Long serviceId, Long userID, Integer year, Integer month);

    public List<Service> findByServiceDescriptionIdAndUserIdOrderByDateOfConsumptionDesc(Long serviceId,Long userId);

    //определенные услуги за определенный год
    public List<Service> findByServiceDescriptionIdAndUserIdAndYearOrderByDateOfConsumptionDesc(Long serviceId,Long userId,Integer year);

    //определенные услуги за определенный месяц
    public List<Service> findByServiceDescriptionIdAndUserIdAndMonthNumberOrderByDateOfConsumptionDesc(Long serviceId,Long userId,Integer monthNumber);

}
