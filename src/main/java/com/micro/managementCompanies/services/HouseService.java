package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.repositories.EntranceRepository;
import com.micro.managementCompanies.repositories.HouseRepository;
import com.micro.managementCompanies.repositories.UserHouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {
    HouseRepository houseRepository;
    UserHouseRepository userHouseRepository;
    EntranceRepository entranceRepository;

    public HouseService(HouseRepository houseRepository, UserHouseRepository userHouseRepository, EntranceRepository entranceRepository) {
        this.houseRepository = houseRepository;
        this.userHouseRepository = userHouseRepository;
        this.entranceRepository = entranceRepository;
    }

    public House getInformationAboutHouse(String region, String town,
                                          String street, Long numberOfHouse){
        return houseRepository.findByRegionAndTownAndStreetAndNumberOfHouse(region,town,street,numberOfHouse).get();
    }

    public List<House> findHousesByUserId(Long id){
        return userHouseRepository.getAllHouseForUser(id);
    }

    public List<House_User> findAllHouse_User(Long houseId){
        return houseRepository.findAllHouse_User(houseId);
    }

    //находим дом по id
    public House findHouseById(Long id){
        return houseRepository.findById(id).get();
    }

    //сохраняем дом
    public House saveHouse(House house){
        return houseRepository.save(house);
    }

    //сохраняем подъезд
    public void saveEntrance(Entrance entrance){
        entranceRepository.save(entrance);
    }

    //сохраняем House_User
    public House_User saveHouse_User(House_User house_user){
        return userHouseRepository.save(house_user);
    }

    //все подъезды
    public List<Entrance> getEntrances(Long houseId) {
        return entranceRepository.findAllByHouseId(houseId);
    }

    //подъезд
    public Entrance getCertainEntrance(Long entranceId) {
        return entranceRepository.findById(entranceId).get();
    }
}
