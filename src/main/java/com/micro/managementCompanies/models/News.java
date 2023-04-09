package com.micro.managementCompanies.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String header;
    private String content;
    private String picture;
    private Date dateOfPublication;

    @OneToMany(mappedBy = "news")
    Set<Tag_News> tag_newsSet;

    @ManyToOne
    ManagementCompany managementCompany;

    @ManyToMany
    List<House> list;
}
