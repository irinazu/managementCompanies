package com.micro.managementCompanies.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String pathForIMgOfChat;


    @OneToMany(mappedBy = "chat")
    Set<Chat_User> chatUserSet;

    @OneToMany(mappedBy = "chat")
    Set<Message> messages;
}
