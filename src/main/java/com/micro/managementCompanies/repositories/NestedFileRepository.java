package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.NestedFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NestedFileRepository extends CrudRepository<NestedFile,Long> {
    @Override
    <S extends NestedFile> S save(S entity);

    @Override
    void deleteById(Long aLong);
}
