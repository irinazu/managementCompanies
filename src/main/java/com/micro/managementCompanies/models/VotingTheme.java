package com.micro.managementCompanies.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class VotingTheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titleOfTheme;

    @OneToMany(mappedBy = "votingTheme")
    List<Voting> votes;
}
