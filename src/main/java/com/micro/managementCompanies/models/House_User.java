package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"house","userSystem"})
public class House_User {
    @EmbeddedId
    private HouseUserKey houseUserKey;
    private Integer numberOfFlat;

    public void setArgs(HouseUserKey houseUserKey, Integer numberOfFlat, House house, UserSystem userSystem,Entrance entrance) {
        this.houseUserKey = houseUserKey;
        this.numberOfFlat = numberOfFlat;
        this.house = house;
        this.userSystem = userSystem;
        this.entrance=entrance;
    }

    @ManyToOne
    @MapsId("houseId")
    @Fetch(value= FetchMode.SELECT)
    @JsonIgnore
    House house;

    @ManyToOne
    @MapsId("userId")
    @Fetch(value= FetchMode.SELECT)
    @JsonIgnore
    UserSystem userSystem;

    @OneToMany(mappedBy = "house_user")
    List<MeteringDevices> meteringDevices;

    @OneToOne(fetch = FetchType.LAZY)
    Entrance entrance;
}
