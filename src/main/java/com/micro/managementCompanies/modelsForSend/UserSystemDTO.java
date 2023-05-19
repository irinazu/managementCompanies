package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.UserSystem;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UserSystemDTO {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String phone;
    private String accountNumber;
    private String town;
    private String street;
    private String house;
    private Integer numberOfFlat;
    private String password;
    private String errorPassword;
    private String errorLogin;
    private RoleDTO roleDTO=new RoleDTO();
    private Boolean flagOnTakeNews;

    private ImageModel imgAvatar;

    public void setAllArgs(ImageModel imgAvatar, String name,Long id) {
        this.imgAvatar = imgAvatar;
        this.name = name;
        this.id=id;
    }


    public void setAllArgs(UserSystem userSystem,Integer numberOfFlat) {
        this.id = userSystem.getId();
        this.name = userSystem.getName();
        this.surname = userSystem.getSurname();
        this.patronymic = userSystem.getPatronymic();
        this.email = userSystem.getEmail();
        this.phone = userSystem.getPhone();
        this.accountNumber = userSystem.getAccountNumber();
        this.town=userSystem.getTown();
        this.house=userSystem.getHouse();
        this.street=userSystem.getStreet();
        if(!userSystem.getRole().getTitle().equals("USER")){
            this.numberOfFlat=userSystem.getNumberOfFlat();
        }else {
            this.numberOfFlat=numberOfFlat;
        }
        this.flagOnTakeNews=userSystem.getFlagOnTakeNews();
        this.roleDTO.setArgs(userSystem.getRole());
    }

    public void setAllArgsFromUserSystem(UserSystem userSystem) {
        this.name = userSystem.getName();
        this.id=userSystem.getId();
    }

    @Override
    public String toString() {
        return "UserSystemDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", numberOfFlat=" + numberOfFlat +
                ", password='" + password + '\'' +
                ", errorPassword='" + errorPassword + '\'' +
                ", errorLogin='" + errorLogin + '\'' +
                ", roleDTO=" + roleDTO +
                ", imgAvatar=" + imgAvatar +
                '}';
    }
}
