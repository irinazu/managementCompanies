package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.modelsForSend.RequestDTO;
import com.micro.managementCompanies.repositories.ChatUserRepository;
import com.micro.managementCompanies.repositories.HouseUserRepository;
import com.micro.managementCompanies.repositories.RoleRepository;
import com.micro.managementCompanies.repositories.UserSystemRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserService {
    UserSystemRepository userSystemRepository;
    ChatUserRepository chatUserRepository;
    EmailMailSender emailMailSender;
    RoleRepository roleRepository;
    HouseUserRepository houseUserRepository;

    public UserService(UserSystemRepository userSystemRepository, ChatUserRepository chatUserRepository, EmailMailSender emailMailSender, RoleRepository roleRepository, HouseUserRepository houseUserRepository) {
        this.userSystemRepository = userSystemRepository;
        this.chatUserRepository = chatUserRepository;
        this.emailMailSender = emailMailSender;
        this.roleRepository = roleRepository;
        this.houseUserRepository = houseUserRepository;
    }

    public UserSystem getUserByName(String name){
        return userSystemRepository.findByName(name);
    }

    public List<UserSystem> getUsersForChat(Long id){
        return chatUserRepository.getAllUser(id);
    }

    //находим по id
    public UserSystem findUserById(Long id){
        return userSystemRepository.findById(id).get();
    }

    //находим по логину
    public UserSystem findUserByEmail(String email){
        if(userSystemRepository.findByEmail(email).isPresent()){
            return userSystemRepository.findByEmail(email).get();
        }
        return null;
    }

    //сообщаем о регистрации
    public void informAboutRegistration(UserSystem userSystem){
        String messageFIO="<div>Здравствуйте, "+userSystem.getSurname()+" "+userSystem.getName()+" "+userSystem.getPatronymic()+"</div>";
        String message="<div>Вы зарегистрированы на портале обслуживания Управляющих компаний</div>";
        String role="  <div>Вы были зарегистрированы как: "+userSystem.getRole().getSimpleTitle()+"</div>";
        String mc="";
        if(userSystem.getRole().getId()!=4L&&userSystem.getRole().getId()!=1L){
            mc="<div>Ваша Управляющая компания: "+userSystem.getManagementCompany().getTitle()+"</div>";
        }
        String password="<div style=\"font-weight: bold\">Ваш пароль: "+userSystem.getPassword()+"</div>";
        String login="<div style=\"font-weight: bold\">Ваш логин: "+userSystem.getEmail()+"</div>";
        String general=messageFIO+message+password+login+role+mc;
        try {
            emailMailSender.send(userSystem.getEmail(),"Регистрация на портале",general);
        }catch (Exception ignored){}
    }

    public void informAboutUpdateRequest(UserSystem userSystem, Request request,String content,ManagementCompany managementCompany){
        String greeting="<div>Здравствуйте, "+userSystem.getSurname()+" "+userSystem.getName()+" "+userSystem.getPatronymic()+"!</div>";
        String update="<div>Вы оставляли заявление на портале, "+"\""+request.getTitle()+"\""+". По нему появилось обновление. </div>";
        String message="<div>Компания "+managementCompany.getTitle()+": "+content+"</div>";
        String general=greeting+update+message;
        try {
            emailMailSender.send(userSystem.getEmail(),"Обновление по заявлению на портале",general);
        }catch (Exception ignored){}
    }

    public void informAboutVote(UserSystem userSystem,Voting voting) {
        String greeting="<div>Здравствуйте, "+userSystem.getSurname()+" "+userSystem.getName()+" "+userSystem.getPatronymic()+"!</div>";
        String about="<div>Появилось новое голосование "+voting.getTitle()+" от управляющей компании "+voting.getManagementCompanyOwnerVoting().getTitle()+" на тему "+ voting.getVotingTheme().getTitleOfTheme()+"</div>";
        String description="<div>"+voting.getDescription()+"</div>";
        String general=greeting+about+description;
        try {
            emailMailSender.send(userSystem.getEmail(),"Новое голосование на портале",general);
        }catch (Exception ignored){}
    }
    //сообщаем о смене Email
    public void informAboutChangeEmail(String email){
        String message="Была произведена смена Email, данный Email был указан как новый";

        try {
            emailMailSender.send(email,"Портал Управляющих компаний",message);
        }catch (Exception ignored){}
    }

    //сообщаем о смене Email
    public void informAboutChangePhone(String email,String phone){
        String message="Была произведена смена номера телефона на: "+phone;

        try {
            emailMailSender.send(email,"Портал Управляющих компаний",message);
        }catch (Exception ignored){}
    }

    //сообщаем о смене Password
    public void informAboutChangePassword(String email){
        String message="Была произведена смена пароля";

        try {
            emailMailSender.send(email,"Портал Управляющих компаний",message);
        }catch (Exception ignored){}
    }

    //сохраняем юзера
    public UserSystem saveUser(UserSystem userSystem){
        return userSystemRepository.save(userSystem);
    }

    //генерируем пароль
    public String generatePassword(){
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();

        // каждая итерация цикла случайным образом выбирает символ из заданного
        // диапазон ASCII и добавляет его к экземпляру `StringBuilder`
        return IntStream.range(0, 10)
                .map(i -> random.nextInt(chars.length()))
                .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
                .collect(Collectors.joining());
    }

    //ищем роль по id
    public Role getRoleById(Long idRole){
        return roleRepository.findById(idRole).get();
    }

    //ищем юзера по лицевому счету
    public Boolean findExistUserSystem(String accountNumber,Long idMC){
        return houseUserRepository.findExistUserSystem(accountNumber, idMC).isPresent();
    }

    //ищем юзера по лицевому счету и логину
    public UserSystem findExistUserSystemReturnUser(String accountNumber,Long idMC){
        return houseUserRepository.findExistUserSystem(accountNumber, idMC).get();
    }

    //должности для ук
    public List<Role> getPostsForMC(){
        return roleRepository.findAllByPostForMc(true);
    }


}
