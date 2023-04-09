package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class VotingOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String option;
    private Long voteNumber=0L;

    @ManyToOne
    Voting voting;

    @OneToMany(mappedBy = "votingOption")
    List<Voting_User> voting_userSet;
}
