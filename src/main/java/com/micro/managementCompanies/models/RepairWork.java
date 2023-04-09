package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class RepairWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String date;
    String description;
    String company;
    String linkOfPhotoBefore;
    String linkOfPhotoAfter;
    String kindOfWork;

    @ManyToOne
    @JsonIgnore
    House house;

    @ManyToOne
    @JsonIgnore
    Entrance entrance;
}
