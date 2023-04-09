package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.House;
import com.micro.managementCompanies.models.RepairWork;
import com.micro.managementCompanies.repositories.HouseRepository;
import com.micro.managementCompanies.repositories.UserHouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {
    HouseRepository houseRepository;
    UserHouseRepository userHouseRepository;

    public HouseService(HouseRepository houseRepository, UserHouseRepository userHouseRepository) {
        this.houseRepository = houseRepository;
        this.userHouseRepository = userHouseRepository;
    }

    public House getInformationAboutHouse(String region, String town,
                                          String street, Long numberOfHouse){
        return houseRepository.findByRegionAndTownAndStreetAndNumberOfHouse(region,town,street,numberOfHouse).get();
    }

    public List<House> findHousesByUserId(Long id){
        return userHouseRepository.getAllHouseForUser(id);
    }
}
