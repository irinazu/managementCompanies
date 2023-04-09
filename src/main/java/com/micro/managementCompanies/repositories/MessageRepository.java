package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends CrudRepository<Message,Long> {
    @Override
    <S extends Message> S save(S entity);

    @Override
    Optional<Message> findById(Long aLong);
}
