package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.House;
import com.micro.managementCompanies.models.House_User;
import com.micro.managementCompanies.models.ProviderCompany;
import com.micro.managementCompanies.models.Voting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseRepository extends CrudRepository<House,Long> {

    Optional<House> findByRegionAndTownAndStreetAndNumberOfHouse(String region,String town,
                                                                 String street,Long numberOfHouse);

    @Query("select pr.providerCompanies from House pr where pr.id=:id")
    List<ProviderCompany> getAllByHouse(Long id);

    @Query("select pr.votes from House pr where pr.id=:id")
    List<Voting> getAllVoting(Long id);

    @Query("select hu.house_userSet from House hu where hu.id=:id")
    List<House_User> findAllHouse_User(Long id);
}
