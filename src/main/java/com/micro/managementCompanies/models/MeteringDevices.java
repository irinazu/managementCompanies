package com.micro.managementCompanies.models;


import com.micro.managementCompanies.modelsForSend.MeteringDevicesDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class MeteringDevices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String series;
    private Date startDate;
    private Date endDate;
    private Date checkDate;

    public void setArgs(MeteringDevicesDTO meteringDevicesDTO) {
        this.series = meteringDevicesDTO.getSeries();
        this.startDate = meteringDevicesDTO.getStartDate();
        this.endDate = meteringDevicesDTO.getEndDate();
        this.checkDate = meteringDevicesDTO.getCheckDate();
    }

    @ManyToOne
    MeteringDevicesType meteringDevicesType;

    @ManyToOne
    House_User house_user;

    @OneToMany(mappedBy = "meteringDevice")
    List<Service> services;
}
