package com.micro.managementCompanies.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String pathForIMgOfChat;

    //замена

    @OneToMany(mappedBy = "chat")
    List<Chat_User> chatUserSet;

    public void addInChat_User(Chat_User chat_user){
        this.chatUserSet.add(chat_user);
    }

    @OneToMany(mappedBy = "chat")
    Set<Message> messages;

    /*@OneToOne(fetch = FetchType.LAZY)
    private House house;*/

    /*@OneToOne(fetch = FetchType.LAZY)
    private Entrance entrance;*/
}
