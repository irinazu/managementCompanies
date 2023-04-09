package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Chat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Chat,Long> {
    @Override
    Optional<Chat> findById(Long aLong);

}
