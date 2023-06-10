package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.News;
import com.micro.managementCompanies.models.Tag_News;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagNewsRepository extends CrudRepository<Tag_News,Long> {

    @Override
    void delete(Tag_News entity);

    public List<Tag_News> findAllByTagIdAndNews_ManagementCompanyIdOrderByNewsDesc(Long tagId,Long mcId);

    public List<Tag_News> findAllByTagIdAndNews_CreatorIdOrderByNewsDesc(Long tagId,Long creatorId);

}
