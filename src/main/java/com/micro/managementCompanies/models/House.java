package com.micro.managementCompanies.models;

import com.micro.managementCompanies.modelsForSend.HouseForSend;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"repairWorks","entrances","managementCompany","house_userSet"})
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String repairWork;
    private String problems;
    private String photoOfHouse;
    private String adjoiningTerritory;
    private String photoOfBasement;

    public void setArgs(HouseForSend houseForSend) {
        this.region = houseForSend.getRegion();
        this.town = houseForSend.getTown();
        this.street = houseForSend.getStreet();
        this.numberOfHouse = houseForSend.getNumberOfHouse();
        this.numberOfEntrance = houseForSend.getNumberOfEntrance();
        this.numberOfFloor = houseForSend.getNumberOfFloor();
        this.yearOfConstruction = houseForSend.getYearOfConstruction();
        this.numberOfFlats = houseForSend.getNumberOfFlats();
    }

    @OneToMany(mappedBy = "house")
    @Fetch(value= FetchMode.SELECT)
    List<House_User> house_userSet;

    @ManyToOne
    ManagementCompany managementCompany;

    @OneToMany(mappedBy = "house")
    @Fetch(value=FetchMode.SELECT)
    List<Entrance> entrances;

    @OneToMany(mappedBy = "house")
    @Fetch(value=FetchMode.SELECT)
    Set<RepairWork> repairWorks;

    @ManyToMany
    Set<ProviderCompany> providerCompanies;

    @ManyToMany
    List<Voting> votes;

    @ManyToMany
    List<News> news;

    @OneToOne
    Chat chat;

    public void addVoting(Voting voting) {
        this.votes.add(voting);
        voting.getHouses().add(this);
    }
}
