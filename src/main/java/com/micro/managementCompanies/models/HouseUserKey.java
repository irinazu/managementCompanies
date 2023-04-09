package com.micro.managementCompanies.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class HouseUserKey implements Serializable {
    @Column(name = "house_id")
    Long houseId;

    @Column(name = "user_id")
    Long userId;
}
