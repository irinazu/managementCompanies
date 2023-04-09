package com.micro.managementCompanies.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Date date;

    public Message(String content, Date date, Chat chat, UserSystem user_system) {
        this.content = content;
        this.date = date;
        this.chat = chat;
        this.user_system = user_system;
    }

    @ManyToOne
    Chat chat;

    @ManyToOne
    UserSystem user_system;

    @OneToMany(mappedBy = "message", cascade = CascadeType.REMOVE)
    Set<NestedFile> nestedFileSet;

    public Message() {}
}
