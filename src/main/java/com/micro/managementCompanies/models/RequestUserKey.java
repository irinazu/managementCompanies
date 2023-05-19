package com.micro.managementCompanies.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class RequestUserKey implements Serializable {
    @Column(name = "user_id")
    Long userId;

    @Column(name = "request_id")
    Long requestId;

}
