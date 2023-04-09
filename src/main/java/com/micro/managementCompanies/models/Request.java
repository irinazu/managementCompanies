package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Setter
@Getter
@Entity
@EqualsAndHashCode(exclude = {"request_userSet","managementCompanyRequest"})
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String typeOfRequest;
    private String town;
    private String street;
    private String house;
    private String flat;
    private String files;
    private String commentary;
    private String publicRequest;
    private LocalDate date;
    private LocalTime time;

    @OneToMany(mappedBy = "request")
    Set<Request_User> request_userSet;

    @ManyToOne
    ManagementCompany managementCompanyRequest;

    @OneToMany(mappedBy = "request")
    @JsonIgnore
    Set<RequestUpdate> requestUpdates;
}
