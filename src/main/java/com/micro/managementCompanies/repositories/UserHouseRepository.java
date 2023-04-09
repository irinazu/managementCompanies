package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.House;
import com.micro.managementCompanies.models.House_User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserHouseRepository extends CrudRepository<House_User,Long> {
    @Query("select u.house from House_User u where u.userSystem.id=:id")
    public List<House> getAllHouseForUser(Long id);
}
