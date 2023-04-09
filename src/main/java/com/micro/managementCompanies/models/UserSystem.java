package com.micro.managementCompanies.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    private String password;
    private String post;
    private String pathForAvatarImg;

    @OneToMany(mappedBy = "userSystem")
    Set<Chat_User> chatUserSet;

    @OneToMany(mappedBy = "userSystem")
    Set<House_User> house_userSet;

    @OneToMany(mappedBy = "userSystem")
    Set<Service_User_Pay> payForServiceSet;

    @OneToMany(mappedBy = "userSystem")
    Set<Request_User> request_userSet;

    //ЛИШНЕЕ?
    @ManyToOne
    ManagementCompany managementCompany;

    @OneToMany(mappedBy = "user_system")
    Set<Message> messages;

    @OneToMany(mappedBy = "userSystem")
    Set<Voting_User> voting_userSet;
}
