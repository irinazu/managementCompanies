package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.UserSystem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UserSystemDTO {
    private ImageModel imgAvatar;
    private String name;
    private Long id;

    public void setAllArgs(ImageModel imgAvatar, String name,Long id) {
        this.imgAvatar = imgAvatar;
        this.name = name;
        this.id=id;
    }

    public void setAllArgsFromUserSystem(UserSystem userSystem) {
        this.name = userSystem.getName();
        this.id=userSystem.getId();
    }
}
