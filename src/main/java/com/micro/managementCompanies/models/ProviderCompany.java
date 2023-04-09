package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class ProviderCompany {
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

    @ManyToMany
    @JsonIgnore
    Set<House> houseSetForProvider;

    @ManyToOne
    ServiceDescription serviceDescriptionForProvider;
}
