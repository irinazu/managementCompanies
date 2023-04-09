package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.Voting;
import com.micro.managementCompanies.models.VotingOption;
import com.micro.managementCompanies.models.Voting_User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Component
@Getter
@Setter
public class VotingOptionDTO {
    private Long id;

    private String option;
    private Long voteNumber;

    public void setArgs(VotingOption votingOption) {
        this.id = votingOption.getId();
        this.option = votingOption.getOption();
        this.voteNumber = votingOption.getVoteNumber();
    }

    UserSystemDTO userSystemDTO;
    private Boolean answerCheck=false;
}
