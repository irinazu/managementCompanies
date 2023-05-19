package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Float tariff;
    private Integer consumption;
    private String month;
    private Date dateOfConsumption;
    private Float dutyForThisMonth;
    private Boolean repaid=false;
    private Float generalDutyForService=0F;
    private String unit;
    private Integer year;
    private Integer monthNumber;


    @OneToMany(mappedBy = "service")
    @JsonIgnore
    Set<Service_User_Pay> payForServiceSet;

    @ManyToOne
    ServiceDescription serviceDescription;

    @ManyToOne
    UserSystem user;

    @ManyToOne
    MeteringDevices meteringDevice;
}
