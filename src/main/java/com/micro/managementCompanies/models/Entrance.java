package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = "repairWorks")
public class Entrance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer numberOfEntrance;
    String photoLinkEntrance;
    String photoLinkOfLift;

    @ManyToOne
    @JsonIgnore
    House house;

    @OneToMany(mappedBy = "entrance")
    @Fetch(value= FetchMode.SELECT)
    Set<RepairWork> repairWorks;


    @OneToOne
    Chat chat;

    /*@OneToOne(mappedBy = "entrance" ,cascade = CascadeType.ALL, orphanRemoval = true)
    House_User house_user;*/


}
