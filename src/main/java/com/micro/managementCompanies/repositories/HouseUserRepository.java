package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Chat_User;
import com.micro.managementCompanies.models.House_User;
import com.micro.managementCompanies.models.UserSystem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface HouseUserRepository extends CrudRepository<House_User,Long> {
    @Query("select u.userSystem from House_User u where u.userSystem.accountNumber=:accountNumber and u.house.managementCompany.id=:idMC")
    public Optional<UserSystem> findExistUserSystem(String accountNumber, Long idMC);

}
