package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.Voting;
import com.micro.managementCompanies.models.VotingOption;
import com.micro.managementCompanies.models.Voting_User;
import com.micro.managementCompanies.repositories.HouseRepository;
import com.micro.managementCompanies.repositories.VotingOptionRepository;
import com.micro.managementCompanies.repositories.Voting_UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {

    HouseRepository houseRepository;
    Voting_UserRepository voting_userRepository;
    VotingOptionRepository votingOptionRepository;

    public VoteService(HouseRepository houseRepository, Voting_UserRepository voting_userRepository, VotingOptionRepository votingOptionRepository) {
        this.houseRepository = houseRepository;
        this.voting_userRepository = voting_userRepository;
        this.votingOptionRepository = votingOptionRepository;
    }

    public List<Voting> getAllVoting(Long id){
        return houseRepository.getAllVoting(id);
    }

    public VotingOption getVotingOption(Long idVoting,Long idUser){
        if(voting_userRepository.getVotingOption(idVoting,idUser).isPresent()){
            return voting_userRepository.getVotingOption(idVoting,idUser).get();
        }
        return null;
    }
    public VotingOption getSimpleVotingOption(Long idVotingOption){
        return votingOptionRepository.findById(idVotingOption).get();
    }

    public Voting_User getVotingOptionUser(Long idUser,Long idOptionVote){
        return voting_userRepository.findByUserSystemIdAndVotingOptionId(idUser,idOptionVote);
    }

    public void saveVotingOptionUser(Voting_User voting_user){
        voting_userRepository.save(voting_user);
    }
    public void saveVotingOption(VotingOption votingOption){
        votingOptionRepository.save(votingOption);
    }

    public List<Voting_User> findByUserSystemId(Long aLong){
        return voting_userRepository.findByUserSystemId(aLong);
    }
}
