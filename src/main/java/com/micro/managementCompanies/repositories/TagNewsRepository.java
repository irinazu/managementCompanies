package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Tag_News;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagNewsRepository extends CrudRepository<Tag_News,Long> {

    @Override
    void delete(Tag_News entity);
}
