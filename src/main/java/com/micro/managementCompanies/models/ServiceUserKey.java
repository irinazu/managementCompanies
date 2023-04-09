package com.micro.managementCompanies.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class ServiceUserKey implements Serializable {
    @Column(name = "service_id")
    Long serviceId;

    @Column(name = "user_id")
    Long userId;

}
