package com.micro.managementCompanies.modelsForSend;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ChatDTO {
    private Long id;
    private String title;
    private ImageModel imageOfChat;

    public void setAllArgs(ImageModel imageOfChat, String title,Long id) {
        this.imageOfChat = imageOfChat;
        this.title = title;
        this.id=id;
    }
}
