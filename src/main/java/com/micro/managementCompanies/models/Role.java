package com.micro.managementCompanies.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String simpleTitle;
    private Boolean postForMc;


    @OneToMany(mappedBy = "role")
    List<UserSystem> userSystems;
}
