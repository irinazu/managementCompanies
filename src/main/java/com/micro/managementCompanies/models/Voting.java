package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Voting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String theme;
    private String description;
    private Date start;
    private Boolean closed=false;
    private String files;
    private Date endOfVoting;

    @OneToMany(mappedBy = "voting")
    List<VotingOption> votingOptionSet;

    @ManyToMany
    List<House> houses;
}
