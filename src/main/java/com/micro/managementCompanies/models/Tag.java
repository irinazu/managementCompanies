package com.micro.managementCompanies.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "tag")
    Set<Tag_News> tag_newsSet;
}
