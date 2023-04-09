package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.RepairWork;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepairWorkRepository extends CrudRepository<RepairWork,Long> {

    RepairWork findByLinkOfPhotoAfterEquals(String link);
}
