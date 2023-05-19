package com.micro.managementCompanies.modelsForSend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.micro.managementCompanies.models.House;
import com.micro.managementCompanies.models.HouseUserKey;
import com.micro.managementCompanies.models.House_User;
import com.micro.managementCompanies.models.UserSystem;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.stereotype.Component;

import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Component
@Getter
@Setter
public class House_UserDTO {

    private Integer numberOfFlat;
    private HouseForSend house=new HouseForSend();
    private UserSystemDTO userSystem=new UserSystemDTO();

    public void setArgs(House_User house_user) {
        this.numberOfFlat = house_user.getNumberOfFlat();
        this.house.setAllArgsOnHouse(house_user.getHouse());
        this.userSystem.setAllArgs(house_user.getUserSystem(),house_user.getNumberOfFlat());
    }
}
