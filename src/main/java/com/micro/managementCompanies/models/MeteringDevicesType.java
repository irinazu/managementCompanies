package com.micro.managementCompanies.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class MeteringDevicesType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String pathImg;

    @OneToMany(mappedBy = "meteringDevicesType")
    List<MeteringDevices> meteringDevices;

    @OneToOne(mappedBy = "meteringDevicesType")
    private ServiceDescription serviceDescription;
}
