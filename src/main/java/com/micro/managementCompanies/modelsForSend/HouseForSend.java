package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.House;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class HouseForSend {

    public void setAllArgs(Long id, String region, String town, String street, Long numberOfHouse,
                        Long numberOfEntrance, Long numberOfFloor, Boolean lift, Integer yearOfConstruction,
                        Long numberOfFlats, String homeCondition, String problems, String adjoiningTerritory) {
        this.id = id;
        this.region = region;
        this.town = town;
        this.street = street;
        this.numberOfHouse = numberOfHouse;
        this.numberOfEntrance = numberOfEntrance;
        this.numberOfFloor = numberOfFloor;
        this.lift = lift;
        this.yearOfConstruction = yearOfConstruction;
        this.numberOfFlats = numberOfFlats;
        this.homeCondition = homeCondition;
        this.problems = problems;
        this.adjoiningTerritory = adjoiningTerritory;
    }

    public void setAllArgsOnHouse(House allArgsOnHouse) {
        this.id = allArgsOnHouse.getId();
        this.region = allArgsOnHouse.getRegion();
        this.town = allArgsOnHouse.getTown();
        this.street = allArgsOnHouse.getStreet();
        this.numberOfHouse = allArgsOnHouse.getNumberOfHouse();
        this.numberOfEntrance = allArgsOnHouse.getNumberOfEntrance();
        this.numberOfFloor = allArgsOnHouse.getNumberOfFloor();
        this.lift = allArgsOnHouse.getLift();
        this.yearOfConstruction = allArgsOnHouse.getYearOfConstruction();
        this.numberOfFlats = allArgsOnHouse.getNumberOfFlats();
        this.homeCondition = allArgsOnHouse.getHomeCondition();
        this.problems = allArgsOnHouse.getProblems();
        this.adjoiningTerritory = allArgsOnHouse.getAdjoiningTerritory();
    }

    private Long id;
    private String region;
    private String town;
    private String street;
    private Long numberOfHouse;
    private Long numberOfEntrance;
    private Long numberOfFloor;
    private Boolean lift;
    private Integer yearOfConstruction;
    private Long numberOfFlats;
    private String homeCondition;
    private String problems;
    private String adjoiningTerritory;
    private List<ImageModel> theHouseItself=new ArrayList<>();
    private BasementForSend basementForSend;
    private RoofForSend roofForSend;
    private  List<EntranceForSend> entranceForSend=new ArrayList<>();

    public void addEntranceForSend(EntranceForSend entrance){
        entranceForSend.add(entrance);
    }
}
