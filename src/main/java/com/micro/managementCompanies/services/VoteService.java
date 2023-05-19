package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {

    HouseRepository houseRepository;
    Voting_UserRepository voting_userRepository;
    VotingOptionRepository votingOptionRepository;
    VotingThemeRepository votingThemeRepository;
    VotingRepository votingRepository;

    public VoteService(HouseRepository houseRepository, Voting_UserRepository voting_userRepository, VotingOptionRepository votingOptionRepository, VotingThemeRepository votingThemeRepository, VotingRepository votingRepository) {
        this.houseRepository = houseRepository;
        this.voting_userRepository = voting_userRepository;
        this.votingOptionRepository = votingOptionRepository;
        this.votingThemeRepository = votingThemeRepository;
        this.votingRepository = votingRepository;
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
    public Voting saveVoting(Voting voting){
        return votingRepository.save(voting);
    }

    public List<Voting_User> findByUserSystemId(Long aLong){
        return voting_userRepository.findByUserSystemId(aLong);
    }

    //находим все темя для голосовний
    public List<VotingTheme> findAllVotingTheme(){
        return (List<VotingTheme>) votingThemeRepository.findAll();
    }

    //находим тему по id
    public VotingTheme findTheme(Long id){
        return votingThemeRepository.findById(id).get();
    }

    //находим ответивших на глосование
    public List<UserSystem> findAllUserSystemAnswered(Long id){
        return voting_userRepository.findAllUserSystemAnswered(id);
    }

    //находим олосование по id
    public Voting findVoting(Long id){
        return votingRepository.findById(id).get();
    }

    //удаляем связь юзера и voting option
    public void deleteVoting_User(Long votingId){
        voting_userRepository.deleteAllByVotingOptionId(votingId);
    }

    //удаляем связь юзера и voting option
    public void deleteVotingOption(Long votingId){
        votingOptionRepository.deleteById(votingId);
    }
}
