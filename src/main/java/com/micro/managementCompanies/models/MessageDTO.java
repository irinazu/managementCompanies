package com.micro.managementCompanies.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Component
@Getter
@Setter
public class MessageDTO {
    Long id;
    String content;
    Long user_system_id;
    Long chat_id;
    List<Long> listImgInNumber;
    Date date;

    public void setArgs(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.user_system_id = message.getUser_system().getId();
        this.chat_id = message.getChat().getId();
        this.date=message.getDate();
    }
}
