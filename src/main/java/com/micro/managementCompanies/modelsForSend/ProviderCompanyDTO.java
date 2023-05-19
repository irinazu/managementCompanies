package com.micro.managementCompanies.modelsForSend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.micro.managementCompanies.models.House;
import com.micro.managementCompanies.models.ProviderCompany;
import com.micro.managementCompanies.models.ServiceDescription;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;

@Component
@Getter
@Setter
public class ProviderCompanyDTO {

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
    ServiceDescriptionDTO serviceDescriptionForProvider=new ServiceDescriptionDTO();

    public void setArgs(ProviderCompany providerCompany) {
        this.id = providerCompany.getId();
        this.title = providerCompany.getTitle();
        this.town = providerCompany.getTown();
        this.street = providerCompany.getStreet();
        this.house = providerCompany.getHouse();
        this.phone = providerCompany.getPhone();
        this.email = providerCompany.getEmail();
        this.nameOfBoss = providerCompany.getNameOfBoss();
        this.patronymicOfBoss = providerCompany.getPatronymicOfBoss();
        this.surnameOfBoss = providerCompany.getSurnameOfBoss();
        this.timeOfWork = providerCompany.getTimeOfWork();
        serviceDescriptionForProvider.setArgs(providerCompany.getServiceDescriptionForProvider());
    }
}
