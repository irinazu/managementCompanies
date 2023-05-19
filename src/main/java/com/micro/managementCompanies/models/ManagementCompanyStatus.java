package com.micro.managementCompanies.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
public class ManagementCompanyStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titleOfStatus;
    private String filePath;

    @OneToMany(mappedBy = "managementCompanyStatus")
    List<ManagementCompany> managementCompanies;

}
