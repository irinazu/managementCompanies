package com.micro.managementCompanies.models;

import com.micro.managementCompanies.modelsForSend.ImageModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Getter
@Setter
public class MessageAndUserDTO {
    Long id;
    String content;
    Date date;
    Long user_system_id;
    Long chat_id;
    String name;
    String surname;
    //user_system_object:UserSystem=new UserSystem();
    List<ImageModel> imageModelsForMessage;

    public void setAvg(Long id, String content, Date date, Long user_system_id, Long chat_id, String name,String surname, List<ImageModel> imageModelsForMessage) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.user_system_id = user_system_id;
        this.chat_id = chat_id;
        this.name = name;
        this.surname = surname;
        this.imageModelsForMessage = imageModelsForMessage;
    }
}
