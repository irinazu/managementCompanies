package com.micro.managementCompanies.repositories;

import com.micro.managementCompanies.models.Service_User_Pay;
import com.micro.managementCompanies.models.UserSystem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentsRepository extends CrudRepository<Service_User_Pay,Long> {

    //@Query("select sup from Service_User_Pay sup where sup.userSystem=:userSystem order by ")
    List<Service_User_Pay> findAllByUserSystemOrderByDateDesc(UserSystem userSystem);
}
