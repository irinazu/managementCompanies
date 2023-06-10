package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.News;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends CrudRepository<News,Long> {

    public List<News> findAllByManagementCompanyIdOrderByDateOfPublicationDesc(Long id);

    public List<News> findAllByCreatorIdOrderByDateOfPublicationDesc(Long id);

}
