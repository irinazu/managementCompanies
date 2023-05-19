package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Data
@Entity
public class Service_User_Pay {
    @EmbeddedId
    ServiceUserKey serviceUserKey;

    @ManyToOne
    @MapsId("serviceId")
    Service service;

    @ManyToOne
    @MapsId("userId")
    @JsonIgnore
    UserSystem userSystem;

    private Date date;
    private Float summa;
}
