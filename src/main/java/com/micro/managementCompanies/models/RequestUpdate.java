package com.micro.managementCompanies.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class RequestUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime time;
    private String status;
    private String commentary;
    private String file;
    private Boolean userUpdate;
    private Boolean managementCompanyUpdate;

    @ManyToOne
    Request request;



}
