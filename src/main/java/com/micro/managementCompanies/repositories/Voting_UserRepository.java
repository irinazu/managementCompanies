package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.House;
import com.micro.managementCompanies.models.VotingOption;
import com.micro.managementCompanies.models.Voting_User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Voting_UserRepository extends CrudRepository<Voting_User,Long> {
    @Query("select u.votingOption from Voting_User u where u.votingOption.voting.id=:OfVoting and u.userSystem.id=:userId")
    public Optional<VotingOption> getVotingOption(Long OfVoting, Long userId);


    public Voting_User findByUserSystemIdAndVotingOptionId(Long userId, Long voteOptionId);

    public List<Voting_User> findByUserSystemId(Long userId);
}
