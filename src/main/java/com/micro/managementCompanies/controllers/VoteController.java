package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.modelsForSend.*;
import com.micro.managementCompanies.repositories.UserHouseRepository;
import com.micro.managementCompanies.services.HouseService;
import com.micro.managementCompanies.services.UserService;
import com.micro.managementCompanies.services.VoteService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

        return generateVotingDTO(votingList,1L);
    }

    //обновляем выбор в голосовании
    @GetMapping("updateVoteOption/{optionId}/{oldOptionId}/{idUser}")
    public void updateVoteOption(@PathVariable("optionId") Long optionId,
                                 @PathVariable("oldOptionId") Long oldOptionId,
                                 @PathVariable("idUser") Long idUser){
        VotingOption votingOption = voteService.getSimpleVotingOption(optionId);
        votingOption.setVoteNumber(votingOption.getVoteNumber() + 1);
        voteService.saveVotingOption(votingOption);

        if(oldOptionId!=-1) {
            Voting_User voting_user = voteService.getVotingOptionUser(idUser, oldOptionId);
            voting_user.setVotingOption(votingOption);
            voteService.saveVotingOptionUser(voting_user);
            VotingOption oldVotingOption = voteService.getSimpleVotingOption(oldOptionId);
            oldVotingOption.setVoteNumber(oldVotingOption.getVoteNumber() - 1);
            voteService.saveVotingOption(oldVotingOption);
        }else {
            Voting_User voting_user =new Voting_User();
            voting_user.setVotingOption(votingOption);
            voting_user.setUserSystem(userService.findUserById(idUser));
            voteService.saveVotingOptionUser(voting_user);

        }
    }

    public List<VotingDTO> generateVotingDTO(List<Voting> votingList,Long idUserForCheckImg){
        List<VotingDTO> votingListForSend=new ArrayList<>();

        for(Voting vote:votingList){
            VotingDTO votingDTO=new VotingDTO();
            votingDTO.setArgs(vote);
            Integer sumAllMustAnswer=0;
            Integer answered=0;


            for (House house:vote.getHouses()){
                sumAllMustAnswer+=house.getHouse_userSet().size();

                HouseForSend houseForSend=new HouseForSend();
                houseForSend.setAllArgsOnHouse(house);
                votingDTO.addInHouses(houseForSend);
            }

            for(VotingOption votingOption:vote.getVotingOptionSet()){
                VotingOptionDTO votingOptionDTO=new VotingOptionDTO();
                votingOptionDTO.setArgs(votingOption);

                if(idUserForCheckImg!=null){
                    VotingOption forCompare=voteService.getVotingOption(vote.getId(),idUserForCheckImg);
                    if(forCompare!=null&&forCompare.getId().equals(votingOption.getId())){
                        votingOptionDTO.setAnswerCheck(true);
                    }
                }

                if(votingOption.getVoting_userSet()!=null){
                    List<UserSystemDTO> userSystemDTOS=new ArrayList<>();

                    for (UserSystem userSystem:voteService.findAllUserSystemAnswered(votingOption.getId())) {
                        answered+=1;
                        UserSystemDTO userSystemDTO=new UserSystemDTO();
                        userSystemDTO.setAllArgs(userSystem,0);
                        userSystemDTOS.add(userSystemDTO);
                    }
                    votingOptionDTO.setUserSystemDTOS(userSystemDTOS);
                }
                votingDTO.setAllMustAnswer(sumAllMustAnswer);
                votingDTO.setAnswered(answered);

                votingDTO.addInVotingOptionSet(votingOptionDTO);
            }


            votingListForSend.add(votingDTO);
        }

        return votingListForSend;
    }

    //берем все темя для голосования
    @GetMapping("getAllThemeVoting")
    public List<VotingThemeDTO> getAllThemeVoting(){
        List<VotingTheme> votingThemes=voteService.findAllVotingTheme();
        List<VotingThemeDTO> votingThemeDTOS=new ArrayList<>();

        for (VotingTheme votingTheme : votingThemes){
            VotingThemeDTO votingThemeDTO=new VotingThemeDTO();
            votingThemeDTO.setArgs(votingTheme);
            votingThemeDTOS.add(votingThemeDTO);
        }
        return votingThemeDTOS;
    }


    @PostMapping("createVoting/{idUser}")
    public void createVoting(@PathVariable("idUser") Long idUser,
                             @RequestBody VotingDTO votingDTO){
        Voting voting=new Voting();
        voting.setArgs(votingDTO);

        //находим УК
        UserSystem userSystem=userService.findUserById(idUser);
        voting.setManagementCompanyOwnerVoting(userSystem.getManagementCompany());
        //находим тему
        VotingTheme votingTheme=voteService.findTheme(votingDTO.getVotingThemeDTO().getId());
        voting.setVotingTheme(votingTheme);
        //находим дома
        List<House> housesForVoting=new ArrayList<>();
        if(votingDTO.getHouses().size()!=0){
            for (HouseForSend houseForSend:votingDTO.getHouses()) {
                House house=houseService.findHouseById(houseForSend.getId());
                housesForVoting.add(house);
            }
            voting.setHouses(housesForVoting);
        }

        voting=voteService.saveVoting(voting);
        if(!housesForVoting.isEmpty()){
            for (House house:housesForVoting) {
                List<Voting> votingList=house.getVotes();
                votingList.add(voting);
                house.setVotes(votingList);
                houseService.saveHouse(house);
            }
        }

        for (VotingOptionDTO votingOptionDTO:votingDTO.getVotingOptionSet()) {
            VotingOption votingOption=new VotingOption();
            votingOption.setOption(votingOptionDTO.getOption());
            votingOption.setVoting(voting);
            voteService.saveVotingOption(votingOption);
        }
    }

    //берем голосования в зависимости от условий
    @GetMapping("getAllVoteWithMode/{mode}/{idUser}/{role}")
    public List<VotingDTO> getAllVoteForMCWithMode(@PathVariable("mode") String mode,
                                                   @PathVariable("idUser") Long idUser,
                                                   @PathVariable("role") String role){
        List<Voting> votingList=new ArrayList<>();
        List<Voting> votingListWithMode=new ArrayList<>();
        List<VotingDTO> forReturn=new ArrayList<>();

        if(role.equals("USER")){
            List<House> houseList=houseService.findHousesByUserId(idUser);
            for(House house:houseList){
                votingList.addAll(house.getVotes());
            }
            votingListWithMode=checkMode(votingList,mode);
            forReturn=generateVotingDTO(votingListWithMode,idUser);

        }else {
            UserSystem userSystem=userService.findUserById(idUser);
            ManagementCompany managementCompany=userSystem.getManagementCompany();
            votingList=managementCompany.getVotingList();
            votingListWithMode=checkMode(votingList,mode);
            forReturn=generateVotingDTO(votingListWithMode,null);

        }
        return forReturn;
    }

    public List<Voting> checkMode(List<Voting> votingList,String mode){

        List<Voting> votingListWithMode=new ArrayList<>();
        for (Voting voting:votingList) {
            if(mode.equals("true")){
                if(voting.getEndOfVoting().getTime()>new Date().getTime()){
                    votingListWithMode.add(voting);
                }
            }else {
                if(voting.getEndOfVoting().getTime()<new Date().getTime()){
                    votingListWithMode.add(voting);
                }
            }
        }
        return votingListWithMode;
    }

    @GetMapping("getCertainVoting/{voteId}")
    public VotingDTO getCertainVoting(@PathVariable("voteId") Long voteId){
        Voting voting=voteService.findVoting(voteId);

        VotingDTO votingDTO=new VotingDTO();
        votingDTO.setArgs(voting);

        //добавляем дома
        for (House house:voting.getHouses()) {
            HouseForSend houseForSend=new HouseForSend();
            houseForSend.setAllArgsOnHouse(house);
            votingDTO.addInHouses(houseForSend);
        }

        //добавляем варианты ответа
        for (VotingOption votingOption:voting.getVotingOptionSet()) {
            VotingOptionDTO votingOptionDTO=new VotingOptionDTO();
            votingOptionDTO.setArgs(votingOption);
            votingDTO.addInVotingOptionSet(votingOptionDTO);
        }

        return votingDTO;
    }

    @PostMapping("updateVoting")
    public void updateVoting(@RequestBody VotingDTO votingDTO){
        Voting voting=voteService.findVoting(votingDTO.getId());
        voting.setArgsUpdate(votingDTO,voteService.findTheme(votingDTO.getVotingThemeDTO().getId()));

        //option
            //новые
        for (VotingOptionDTO votingOptionDTO:votingDTO.getVotingOptionSet()) {
            if(votingOptionDTO.getId()==0){
                VotingOption option = new VotingOption();
                option.setOption(votingOptionDTO.getOption());
                option.setVoting(voting);
                voteService.saveVotingOption(option);
            }
        }
            //удаление старых
        for (VotingOptionDTO votingOptionDTO:votingDTO.getDeleteOptionsMustDeletingFromServer()) {
            voteService.deleteVoting_User(votingOptionDTO.getId());
            voteService.deleteVotingOption(votingOptionDTO.getId());
        }

        //house
            //новые
        List<House> housesForVoting=voting.getHouses();
        if(votingDTO.getHousesMustAdding().size()!=0){
            for (HouseForSend houseForSend:votingDTO.getHousesMustAdding()) {
                House house=houseService.findHouseById(houseForSend.getId());
                house.addVoting(voting);
                houseService.saveHouse(house);
                //housesForVoting.add(house);
            }
            //voting.setHouses(housesForVoting);
        }
        //voting=voteService.saveVoting(voting);

        /*if(!housesForVoting.isEmpty()){
            for (House house:housesForVoting) {
                List<Voting> votingList=house.getVotes();
                votingList.add(voting);
                house.setVotes(votingList);
                houseService.saveHouse(house);
            }
        }*/

            //удаляем
        if(votingDTO.getHousesMustDeleting().size()!=0){
            for (HouseForSend houseForSend:votingDTO.getHousesMustDeleting()) {
                House house=houseService.findHouseById(houseForSend.getId());
                voting.removeHouse(house);
                houseService.saveHouse(house);
            }
        }

        voting=voteService.saveVoting(voting);

    }

}

