package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.Voting;
import com.micro.managementCompanies.models.VotingOption;
import com.micro.managementCompanies.models.Voting_User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

@Component
@Getter
@Setter
public class VotingOptionDTO {
    private Long id;

    private String option;
    private Long voteNumber;
    private List<UserSystemDTO> userSystemDTOS;

    public void setArgs(VotingOption votingOption) {
        this.id = votingOption.getId();
        this.option = votingOption.getOption();
        this.voteNumber = votingOption.getVoteNumber();
    }

    private Boolean answerCheck=false;

    @Override
    public String toString() {
        return "VotingOptionDTO{" +
                "id=" + id +
                ", option='" + option + '\'' +
                ", voteNumber=" + voteNumber +
                ", userSystemDTOS=" + userSystemDTOS +
                ", answerCheck=" + answerCheck +
                '}';
    }
}
