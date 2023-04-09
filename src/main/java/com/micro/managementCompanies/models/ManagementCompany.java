package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class ManagementCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String town;
    private String street;
    private String house;
    private Integer phone;
    private String email;
    private String nameOfBoss;
    private String patronymicOfBoss;
    private String surnameOfBoss;
    private String timeOfWork;

    @OneToMany(mappedBy = "managementCompany")
    @JsonIgnore
    Set<House> houses;

    @OneToMany(mappedBy = "managementCompany")
    @JsonIgnore
    Set<UserSystem> userSystems;

    @OneToMany(mappedBy = "managementCompanyRequest")
    @JsonIgnore
    Set<Request> requests;

    @OneToMany(mappedBy = "managementCompany")
    List<News> news;
}
