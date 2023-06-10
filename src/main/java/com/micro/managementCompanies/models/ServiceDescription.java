package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
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
    private String unit;


    @OneToMany(mappedBy = "serviceDescription")
    @JsonIgnore
    List<Service> serviceSet;

    @OneToMany(mappedBy = "serviceDescriptionForProvider")
    @JsonIgnore
    Set<ProviderCompany> providerCompanySet;

    @OneToOne
    MeteringDevicesType meteringDevicesType;
}
