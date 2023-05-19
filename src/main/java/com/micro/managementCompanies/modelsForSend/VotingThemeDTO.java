package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.Voting;
import com.micro.managementCompanies.models.VotingTheme;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Component
@Getter
@Setter
public class VotingThemeDTO {
    private Long id;
    private String titleOfTheme;

    public void setArgs(VotingTheme votingTheme) {
        this.id = votingTheme.getId();
        this.titleOfTheme = votingTheme.getTitleOfTheme();
    }
}
