package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class ServiceDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String imgUrl;
    private String counter;


    @OneToMany(mappedBy = "serviceDescription")
    @JsonIgnore
    Set<Service> serviceSet;

    @OneToMany(mappedBy = "serviceDescriptionForProvider")
    @JsonIgnore
    Set<ProviderCompany> providerCompanySet;
}
