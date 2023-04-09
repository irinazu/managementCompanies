package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.modelsForSend.HouseForSend;
import com.micro.managementCompanies.modelsForSend.VotingDTO;
import com.micro.managementCompanies.modelsForSend.VotingOptionDTO;
import com.micro.managementCompanies.repositories.UserHouseRepository;
import com.micro.managementCompanies.services.HouseService;
import com.micro.managementCompanies.services.UserService;
import com.micro.managementCompanies.services.VoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("vote")
public class VoteController {
    VoteService voteService;
    HouseService houseService;
    UserService userService;

    public VoteController(VoteService voteService, HouseService houseService, UserService userService) {
        this.voteService = voteService;
        this.houseService = houseService;
        this.userService = userService;
    }

    @GetMapping("getAllVoteForUser")
    public List<VotingDTO> getLastDataForCounter(){
        List<House> houseList=houseService.findHousesByUserId(1L);
        List<Voting> votingList=new ArrayList<>();
        for(House house:houseList){
            votingList.addAll(house.getVotes());
        }

        return generateVotingDTO(votingList);
    }

    //обновляем выбор в голосовании
    @GetMapping("updateVoteOption/{optionId}/{oldOptionId}")
    public void updateVoteOption(@PathVariable("optionId") Long optionId,
                                 @PathVariable("oldOptionId") Long oldOptionId){
        VotingOption votingOption = voteService.getSimpleVotingOption(optionId);
        votingOption.setVoteNumber(votingOption.getVoteNumber() + 1);
        voteService.saveVotingOption(votingOption);

        if(oldOptionId!=-1) {
            Voting_User voting_user = voteService.getVotingOptionUser(1L, oldOptionId);
            voting_user.setVotingOption(votingOption);
            voteService.saveVotingOptionUser(voting_user);
            VotingOption oldVotingOption = voteService.getSimpleVotingOption(oldOptionId);
            oldVotingOption.setVoteNumber(oldVotingOption.getVoteNumber() - 1);
            voteService.saveVotingOption(oldVotingOption);
        }else {
            Voting_User voting_user =new Voting_User();
            voting_user.setVotingOption(votingOption);
            voting_user.setUserSystem(userService.findUserById(1L));
            voteService.saveVotingOptionUser(voting_user);

        }
    }

    public List<VotingDTO> generateVotingDTO(List<Voting> votingList){
        List<VotingDTO> votingListForSend=new ArrayList<>();
        for(Voting vote:votingList){
            VotingDTO votingDTO=new VotingDTO();
            votingDTO.setArgs(vote);
            for (House house:vote.getHouses()){
                HouseForSend houseForSend=new HouseForSend();
                houseForSend.setAllArgsOnHouse(house);
                votingDTO.addInHouses(houseForSend);
            }

            for(VotingOption votingOption:vote.getVotingOptionSet()){
                VotingOptionDTO votingOptionDTO=new VotingOptionDTO();
                votingOptionDTO.setArgs(votingOption);
                VotingOption forCompare=voteService.getVotingOption(vote.getId(),1L);
                if(forCompare!=null&&forCompare.getId().equals(votingOption.getId())){
                    votingOptionDTO.setAnswerCheck(true);
                }
                votingDTO.addInVotingOptionSet(votingOptionDTO);
            }
            votingListForSend.add(votingDTO);
        }

        return votingListForSend;
    }
}

