package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.House;
import com.micro.managementCompanies.models.Voting;
import com.micro.managementCompanies.models.VotingOption;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@Getter
@Setter
public class VotingDTO {
    private Long id;

    private String title;
    private String theme;
    private String description;
    private Date start;
    private Boolean closed=false;
    private String files;
    private Date endOfVoting;

    public void setArgs(Voting voting) {
        this.id = voting.getId();
        this.title = voting.getTitle();
        this.theme = voting.getTheme();
        this.description = voting.getDescription();
        this.start = voting.getStart();
        this.closed = voting.getClosed();
        this.files = voting.getFiles();
        this.endOfVoting = voting.getEndOfVoting();
    }

    public void addInVotingOptionSet(VotingOptionDTO votingOptionDTO){
        votingOptionSet.add(votingOptionDTO);
    }
    public void addInHouses(HouseForSend houseForSend){
        houses.add(houseForSend);
    }

    List<VotingOptionDTO> votingOptionSet=new ArrayList<>();
    List<HouseForSend> houses=new ArrayList<>();
}
