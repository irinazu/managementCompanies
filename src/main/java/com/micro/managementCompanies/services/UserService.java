package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.House;
import com.micro.managementCompanies.models.UserSystem;
import com.micro.managementCompanies.repositories.ChatUserRepository;
import com.micro.managementCompanies.repositories.UserSystemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    UserSystemRepository userSystemRepository;
    ChatUserRepository chatUserRepository;

    public UserService(UserSystemRepository userSystemRepository, ChatUserRepository chatUserRepository) {
        this.userSystemRepository = userSystemRepository;
        this.chatUserRepository = chatUserRepository;
    }

    public UserSystem getUserByName(String name){
        return userSystemRepository.findByName(name);
    }

    public List<UserSystem> getUsersForChat(Long id){
        return chatUserRepository.getAllUser(id);
    }

    public UserSystem findUserById(Long id){
        return userSystemRepository.findById(id).get();
    }


}
