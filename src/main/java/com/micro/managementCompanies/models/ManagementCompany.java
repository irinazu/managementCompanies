package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.micro.managementCompanies.modelsForSend.ManagementCompanyDTO;
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
    private String inn;
    private String kpp;
    private String checkingAccount;
    private String correspondentAccount;
    private String bankTitle;
    private String bicBank;
    private String causeRejection;

    public void setArgs(ManagementCompanyDTO managementCompanyDTO) {
        this.title = managementCompanyDTO.getTitle();
        this.town = managementCompanyDTO.getTown();
        this.street = managementCompanyDTO.getStreet();
        this.house = managementCompanyDTO.getHouse();
        this.phone = managementCompanyDTO.getPhone();
        this.email = managementCompanyDTO.getEmail();
        this.inn = managementCompanyDTO.getInn();
        this.kpp = managementCompanyDTO.getKpp();
        this.checkingAccount = managementCompanyDTO.getCheckingAccount();
        this.correspondentAccount = managementCompanyDTO.getCorrespondentAccount();
        this.bankTitle = managementCompanyDTO.getBankTitle();
        this.bicBank = managementCompanyDTO.getBicBank();
    }

    @OneToMany(mappedBy = "managementCompany")
    @JsonIgnore
    List<House> houses;

    @OneToMany(mappedBy = "managementCompany")
    @JsonIgnore
    List<UserSystem> userSystems;

    @OneToMany(mappedBy = "managementCompanyRequest")
    @JsonIgnore
    Set<Request> requests;

    @OneToMany(mappedBy = "managementCompany")
    List<News> news;

    @OneToMany(mappedBy = "managementCompanyOwnerVoting")
    List<Voting> votingList;

    @ManyToOne
    UserSystem head;

    @ManyToOne
    ManagementCompanyStatus managementCompanyStatus;
}
