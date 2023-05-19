package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Request;
import com.micro.managementCompanies.models.RequestTheme;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestThemeRepository extends CrudRepository<RequestTheme,Long> {
    @Override
    Iterable<RequestTheme> findAll();
}
