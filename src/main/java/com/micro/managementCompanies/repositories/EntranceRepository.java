package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Entrance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntranceRepository extends CrudRepository<Entrance,Long> {

    public List<Entrance> findAllByHouseId(Long houseId);
}
