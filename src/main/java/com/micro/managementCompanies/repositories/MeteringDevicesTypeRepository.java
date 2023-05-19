package com.micro.managementCompanies.repositories;


import com.micro.managementCompanies.models.MeteringDevices;
import com.micro.managementCompanies.models.MeteringDevicesType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeteringDevicesTypeRepository extends CrudRepository<MeteringDevicesType,Long> {
}
