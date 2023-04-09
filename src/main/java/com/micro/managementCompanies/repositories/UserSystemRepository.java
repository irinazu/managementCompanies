package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.UserSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSystemRepository extends JpaRepository<UserSystem, Long> {
    UserSystem findByName(String name);
    Optional<UserSystem> findById(Long id);


}
