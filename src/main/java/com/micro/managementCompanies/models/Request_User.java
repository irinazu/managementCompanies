package com.micro.managementCompanies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Request_User {
    @EmbeddedId
    RequestUserKey requestUserKey;

    @ManyToOne
    @MapsId("userId")
    @JsonIgnore
    UserSystem userSystem;

    @ManyToOne
    @MapsId("requestId")
    @JsonIgnore
    Request request;


    public void setArgs(RequestUserKey requestUserKey, UserSystem userSystem, Request request) {
        this.requestUserKey = requestUserKey;
        this.userSystem = userSystem;
        this.request = request;
    }

}
