package com.micro.managementCompanies.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Chat_User {
    @EmbeddedId
    ChatUserKey id;
    Date date;

    @ManyToOne
    @MapsId("chatId")
    Chat chat;

    @ManyToOne
    @MapsId("userId")
    UserSystem userSystem;

    private String message;
    private String file;
}
