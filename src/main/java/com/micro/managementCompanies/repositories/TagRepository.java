package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<Tag,Long> {

}
