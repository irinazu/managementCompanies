package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.Chat;
import com.micro.managementCompanies.models.Chat_User;
import com.micro.managementCompanies.models.Message;
import com.micro.managementCompanies.models.NestedFile;
import com.micro.managementCompanies.repositories.ChatRepository;
import com.micro.managementCompanies.repositories.ChatUserRepository;
import com.micro.managementCompanies.repositories.MessageRepository;
import com.micro.managementCompanies.repositories.NestedFileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    ChatRepository chatRepository;
    ChatUserRepository chatUserRepository;
    MessageRepository messageRepository;
    NestedFileRepository nestedFileRepository;

    public ChatService(ChatRepository chatRepository, ChatUserRepository chatUserRepository, MessageRepository messageRepository, NestedFileRepository nestedFileRepository) {
        this.chatRepository = chatRepository;
        this.chatUserRepository = chatUserRepository;
        this.messageRepository = messageRepository;
        this.nestedFileRepository = nestedFileRepository;
    }

    public Chat findById(Long id){
        return chatRepository.findById(id).get();
    }

    //все чаты для определенного user
    public List<Chat> getAllChatForUser(Long id){
        return chatUserRepository.getAllChatForUser(id);
    }

    public Message getLastMessageForChat(Long chatId){
        if(messageRepository.findFirstByChatIdOrderByDateDesc(chatId).isPresent()){
            return messageRepository.findFirstByChatIdOrderByDateDesc(chatId).get();
        }else {
            return null;
        }
    }

    public Chat_User getChat(Long idChat,Long idUser){
        return chatUserRepository.getChat(idChat,idUser);
    }

    //сохраняем чат-юзер
    public void saveChatUser(Chat_User chat_user){
        chatUserRepository.save(chat_user);
    }

    //сохраняем сообщение
    public Message saveMessage(Message message){
        return messageRepository.save(message);
    }
    public Message findByIdMessage(Long id){
        return messageRepository.findById(id).get();
    }
    public void deleteByIdMessage(Long id){messageRepository.deleteById(id);}

    public void saveNestedFile(NestedFile nestedFile){
        nestedFileRepository.save(nestedFile);
    }
    public void deleteByIdNestedFile(Long id){nestedFileRepository.deleteById(id);}

    //сохраняем чат
    public Chat saveChat(Chat chat){
        return chatRepository.save(chat);
    }

}
