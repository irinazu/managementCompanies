package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.micro.managementCompanies.modelsForSend.RequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
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
    private String town;
    private String street;
    private String house;
    private String flat;
    private String files;
    private String commentary;
    private String publicRequest;
    private Date date;

    public Request() {
    }

    public Request(RequestDTO requestDTO,ManagementCompany managementCompany,RequestTheme requestTheme) {
        this.title = requestDTO.getTitle();
        this.town = requestDTO.getTown();
        this.street = requestDTO.getStreet();
        this.house = requestDTO.getHouse();
        this.flat = requestDTO.getFlat();
        this.commentary = requestDTO.getCommentary();
        this.date = new Date();
        this.managementCompanyRequest = managementCompany;
        this.requestTheme = requestTheme;
    }

    @OneToMany(mappedBy = "request")
    List<Request_User> request_userSet;

    @ManyToOne
    ManagementCompany managementCompanyRequest;

    @OneToMany(mappedBy = "request")
    @JsonIgnore
    List<RequestUpdate> requestUpdates;

    @ManyToOne
    RequestTheme requestTheme;
}
/*managementCompany
* tdsspnrncekoplic
* */