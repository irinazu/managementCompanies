package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"house","userSystem"})
public class House_User {
    @EmbeddedId
    private HouseUserKey houseUserKey;

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
}
