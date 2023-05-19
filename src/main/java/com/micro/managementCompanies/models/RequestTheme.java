package com.micro.managementCompanies.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
public class RequestTheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titleOfTheme;

    @OneToMany(mappedBy = "requestTheme")
    List<Request> requests;
}
