package com.micro.managementCompanies.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class ChatUserKey implements Serializable {
    Long chatId;
    Long userId;

    public ChatUserKey(Long chatId, Long userId) {
        this.chatId = chatId;
        this.userId = userId;
    }

    public ChatUserKey() {
    }
}
