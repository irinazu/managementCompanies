package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.House_User;
import com.micro.managementCompanies.models.MeteringDevices;
import com.micro.managementCompanies.models.Service;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeteringDevicesRepository extends CrudRepository<MeteringDevices,Long> {

   //public Optional<MeteringDevices> findFirstByMeteringDevicesTypeServiceDescriptionIdAndHouse_userOrderByStartDateDesc(Long serviceId, House_User house_user);


    @Query("select s from MeteringDevices s where s.meteringDevicesType.serviceDescription.id=:service and s.house_user=:house_user order by s.startDate desc")
    public Optional<MeteringDevices> getLastMeteringDevices(Long service, House_User house_user);
}
