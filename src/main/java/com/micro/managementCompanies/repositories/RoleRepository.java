package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long> {
    public List<Role> findAllByPostForMc(Boolean postForMC);
}
