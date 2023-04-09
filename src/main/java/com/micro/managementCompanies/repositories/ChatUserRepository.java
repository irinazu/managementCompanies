package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Chat;
import com.micro.managementCompanies.models.Chat_User;
import com.micro.managementCompanies.models.UserSystem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatUserRepository extends CrudRepository<Chat_User,Long> {
    @Query("select c.userSystem from Chat_User c where c.chat.id=:id")
    List<UserSystem> getAllUser(Long id);

    @Query("select c.chat from Chat_User c where c.userSystem.id=:id")
    List<Chat> getAllChatForUser(Long id);

    @Query("select c from Chat_User c where c.chat.id=:idChat and c.userSystem.id=:idUser")
    Chat_User getChat(Long idChat,Long idUser);
}
