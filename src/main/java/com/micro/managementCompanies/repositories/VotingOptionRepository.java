package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.VotingOption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotingOptionRepository extends CrudRepository<VotingOption,Long> {
    @Override
    Optional<VotingOption> findById(Long aLong);
}
