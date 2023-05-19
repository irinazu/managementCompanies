package com.micro.managementCompanies.models;

import com.micro.managementCompanies.modelsForSend.UserSystemDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"house_userSet"})
public class UserSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String phone;
    private String accountNumber;
    private String town;
    private String street;
    private String house;
    private Integer numberOfFlat;

    private Boolean flagOnTakeNews=false;

    private String password;
    private String pathForAvatarImg;

    public void setArgs(UserSystemDTO userSystemDTO) {
        this.name = userSystemDTO.getName();
        this.surname = userSystemDTO.getSurname();
        this.patronymic = userSystemDTO.getPatronymic();
        this.email = userSystemDTO.getEmail();
        this.phone = userSystemDTO.getPhone();
        this.town = userSystemDTO.getTown();
        this.street = userSystemDTO.getStreet();
        this.house = userSystemDTO.getHouse();
        this.numberOfFlat = userSystemDTO.getNumberOfFlat();
        this.accountNumber=userSystemDTO.getAccountNumber();
    }

    public void setArgsForUser(UserSystemDTO userSystemDTO) {
        this.name = userSystemDTO.getName();
        this.surname = userSystemDTO.getSurname();
        this.patronymic = userSystemDTO.getPatronymic();
        this.accountNumber=userSystemDTO.getAccountNumber();
    }

    @OneToMany(mappedBy = "userSystem")
    Set<Chat_User> chatUserSet;

    @OneToMany(mappedBy = "userSystem")
    List<House_User> house_userSet;

    @OneToMany(mappedBy = "userSystem")
    Set<Service_User_Pay> payForServiceSet;

    @OneToMany(mappedBy = "userSystem")
    List<Request_User> request_userSet;

    @ManyToOne
    ManagementCompany managementCompany;

    @OneToMany(mappedBy = "user_system")
    Set<Message> messages;

    @OneToMany(mappedBy = "userSystem")
    Set<Voting_User> voting_userSet;

    @OneToMany(mappedBy = "creator")
    List<News> newsForCreator;

    @ManyToOne
    Role role;

    @OneToMany(mappedBy = "head")
    List<ManagementCompany> managementCompaniesForHead;

    @OneToMany(mappedBy = "user")
    List<Service> services;

}
