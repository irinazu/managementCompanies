package com.micro.managementCompanies.models;

import com.micro.managementCompanies.modelsForSend.RequestUpdateDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@Entity
public class RequestUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private String commentary;
    private String file;
    private Boolean userUpdate;
    private Boolean managementCompanyUpdate;

    public void setArgs(RequestUpdateDTO requestUpdateDTO,Request request) {
        this.date = new Date();
        this.commentary = requestUpdateDTO.getCommentary();
        this.userUpdate = requestUpdateDTO.getUserUpdate();
        this.managementCompanyUpdate = requestUpdateDTO.getManagementCompanyUpdate();
        this.request = request;
    }

    @ManyToOne
    Request request;

    @ManyToOne
    RequestStatus requestStatus;

}
