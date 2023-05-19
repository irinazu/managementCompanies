package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class RoleDTO {
    private Long id;
    private String title;
    private String simpleTitle;
    private Boolean postForMc;

    public void setArgs(Role role) {
        this.id = role.getId();
        this.title = role.getTitle();
        this.simpleTitle = role.getSimpleTitle();
        this.postForMc = role.getPostForMc();
    }
}
