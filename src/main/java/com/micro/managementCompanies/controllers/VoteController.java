package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.modelsForSend.*;
import com.micro.managementCompanies.repositories.UserHouseRepository;
import com.micro.managementCompanies.services.HouseService;
import com.micro.managementCompanies.services.ManagementCompanyService;
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
    ManagementCompanyService managementCompanyService;

    public VoteController(VoteService voteService, HouseService houseService, UserService userService, ManagementCompanyService managementCompanyService) {
        this.voteService = voteService;
        this.houseService = houseService;
        this.userService = userService;
        this.managementCompanyService = managementCompanyService;
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

        System.out.println(votingList.get(0).getTitle());
        for(Voting vote:votingList){
            VotingDTO votingDTO=new VotingDTO();
            votingDTO.setArgs(vote);
            Integer sumAllMustAnswer=0;
            Integer answered=0;

            List<UserSystem> allAnsweredUserSystem=new ArrayList<>();
            List<UserSystemDTO> notAnsweredUserSystem=new ArrayList<>();

            for (House house:vote.getHouses()){
                sumAllMustAnswer+=house.getHouse_userSet().size();
                for (House_User house_user: house.getHouse_userSet()) {
                    allAnsweredUserSystem.add(house_user.getUserSystem());
                }
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

            List<UserSystemDTO> added=new ArrayList<>();
            for(VotingOptionDTO votingOptionDTO:votingDTO.getVotingOptionSet()){
                added.addAll(votingOptionDTO.getUserSystemDTOS());
            }
            for (int j=0;j<allAnsweredUserSystem.size();j++){
                Long userId=allAnsweredUserSystem.get(j).getId();
                if(!added.stream().anyMatch(x->x.getId()==userId)){
                    UserSystemDTO userSystemDTO=new UserSystemDTO();
                    userSystemDTO.setAllArgs(allAnsweredUserSystem.get(j),0);
                    notAnsweredUserSystem.add(userSystemDTO);
                }
            }
            votingDTO.setNotAnsweredUserSystem(notAnsweredUserSystem);

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


    @PostMapping("createVoting/{idUser}/{idMC}")
    public void createVoting(@PathVariable("idUser") Long idUser,
                             @PathVariable("idMC") Long idMC,
                             @RequestBody VotingDTO votingDTO){
        Voting voting=new Voting();
        voting.setArgs(votingDTO);

        //находим УК
        UserSystem userSystem=userService.findUserById(idUser);
        if(userSystem.getRole().getTitle().equals("DISPATCHER")){
            voting.setManagementCompanyOwnerVoting(userSystem.getManagementCompany());
        }else {
            voting.setManagementCompanyOwnerVoting(managementCompanyService.findManagementCompany(idMC));
        }
        //находим тему
        VotingTheme votingTheme=voteService.findTheme(votingDTO.getVotingThemeDTO().getId());
        voting.setVotingTheme(votingTheme);
        //находим дома
        List<UserSystem> userSystemsForEmail=new ArrayList<>();
        List<House> housesForVoting=new ArrayList<>();
        if(votingDTO.getHouses().size()!=0){
            for (HouseForSend houseForSend:votingDTO.getHouses()) {
                House house=houseService.findHouseById(houseForSend.getId());
                housesForVoting.add(house);
                for (House_User house_user:house.getHouse_userSet()) {
                    userSystemsForEmail.add(house_user.getUserSystem());
                }
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

        for (UserSystem userSystem1:userSystemsForEmail) {
            if(userSystem1.getEmail()!=null){
                userService.informAboutVote(userSystem1,voting);
            }
        }

    }


    //берем голосования в зависимости от условий
    @GetMapping("getAllVoteWithMode/{mode}/{idUser}/{role}/{idMC}")
    public List<VotingDTO> getAllVoteForMCWithMode(@PathVariable("mode") String mode,
                                                   @PathVariable("idUser") Long idUser,
                                                   @PathVariable("role") String role,
                                                   @PathVariable("idMC") Long idMC){
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

        }else if(role.equals("DISPATCHER")){
            UserSystem userSystem=userService.findUserById(idUser);
            ManagementCompany managementCompany=userSystem.getManagementCompany();
            votingList=managementCompany.getVotingList();
            votingListWithMode=checkMode(votingList,mode);
            forReturn=generateVotingDTO(votingListWithMode,null);

        }else {
            ManagementCompany managementCompany=managementCompanyService.findManagementCompany(idMC);
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

    @GetMapping("getCertainVotingForPage/{voteId}/{idUser}")
    public VotingDTO getCertainVotingForPage(@PathVariable("voteId") Long voteId,
                                             @PathVariable("idUser") Long idUser){

        List<Voting> votingList=new ArrayList<>();
        votingList.add(voteService.findVoting(voteId));

        List<VotingDTO> votingDTOS=new ArrayList<>();

        if(userService.findUserById(idUser).getRole().getTitle().equals("USER")){
            votingDTOS=generateVotingDTO(votingList,idUser);
        }else {
            System.out.println(votingList.get(0).getTitle());
            votingDTOS=generateVotingDTO(votingList,null);
        }

        return votingDTOS.get(0);
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

