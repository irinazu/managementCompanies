package com.micro.managementCompanies.models;

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

    @OneToMany(mappedBy = "house")
    @Fetch(value= FetchMode.SELECT)
    Set<House_User> house_userSet;

    @ManyToOne
    ManagementCompany managementCompany;

    @OneToMany(mappedBy = "house")
    @Fetch(value=FetchMode.SELECT)
    Set<Entrance> entrances;

    @OneToMany(mappedBy = "house")
    @Fetch(value=FetchMode.SELECT)
    Set<RepairWork> repairWorks;

    @ManyToMany
    Set<ProviderCompany> providerCompanies;

    @ManyToMany
    List<Voting> votes;

    @ManyToMany
    List<News> news;
}
