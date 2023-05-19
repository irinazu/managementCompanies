package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.micro.managementCompanies.modelsForSend.VotingDTO;
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

    public void setArgs(VotingDTO votingDTO) {
        this.title = votingDTO.getTitle();
        this.description = votingDTO.getDescription();
        this.start = new Date();
        this.endOfVoting = votingDTO.getEndOfVoting();
    }

    public void setArgsUpdate(VotingDTO votingDTO,VotingTheme votingTheme) {
        this.title = votingDTO.getTitle();
        this.description = votingDTO.getDescription();
        this.endOfVoting = votingDTO.getEndOfVoting();
        this.votingTheme = votingTheme;
    }

    @OneToMany(mappedBy = "voting")
    List<VotingOption> votingOptionSet;

    @ManyToMany
    List<House> houses;

    @ManyToOne
    VotingTheme votingTheme;

    @ManyToOne
    ManagementCompany managementCompanyOwnerVoting;

    public void removeHouse(House house) {
        this.houses.remove(house);
        house.getVotes().remove(this);
    }
}
