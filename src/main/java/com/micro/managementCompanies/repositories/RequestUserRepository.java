package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Request_User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestUserRepository extends CrudRepository<Request_User,Long> {
}
