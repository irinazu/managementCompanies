package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.House;
import com.micro.managementCompanies.models.ManagementCompany;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class ManagementCompanyDTO {
    private Long id;

    private String title;
    private String town;
    private String street;
    private String house;
    private Integer phone;
    private String email;
    private String nameOfBoss;
    private String patronymicOfBoss;
    private String surnameOfBoss;
    private String timeOfWork;
    private String inn;
    private String kpp;
    private String checkingAccount;
    private String correspondentAccount;
    private String bankTitle;
    private String bicBank;
    private List<HouseForSend> housesDTO=new ArrayList<>();
    private ManagementCompanyStatusDTO managementCompanyStatusDTO=new ManagementCompanyStatusDTO();
    private String causeRejection;

    public void setArgs(ManagementCompany managementCompany) {
        this.id = managementCompany.getId();
        this.title = managementCompany.getTitle();
        this.town = managementCompany.getTown();
        this.street = managementCompany.getStreet();
        this.house = managementCompany.getHouse();
        this.phone = managementCompany.getPhone();
        this.email = managementCompany.getEmail();
        this.nameOfBoss = managementCompany.getNameOfBoss();
        this.patronymicOfBoss = managementCompany.getPatronymicOfBoss();
        this.surnameOfBoss = managementCompany.getSurnameOfBoss();
        this.timeOfWork = managementCompany.getTimeOfWork();
        this.inn = managementCompany.getInn();
        this.kpp = managementCompany.getKpp();
        this.checkingAccount = managementCompany.getCheckingAccount();
        this.correspondentAccount = managementCompany.getCorrespondentAccount();
        this.bankTitle = managementCompany.getBankTitle();
        this.bicBank = managementCompany.getBicBank();
        this.causeRejection=managementCompany.getCauseRejection();
        createHouseDTO(managementCompany.getHouses());
        managementCompanyStatusDTO.setArgs(managementCompany.getManagementCompanyStatus());

    }

    private void createHouseDTO(List<House> houses){
        for (House house : houses){
            HouseForSend houseForSend=new HouseForSend();
            houseForSend.setAllArgsOnHouse(house);
            housesDTO.add(houseForSend);
        }
    }

    @Override
    public String toString() {
        return "ManagementCompanyDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", town='" + town + '\'' +
                ", street='" + street + '\'' +
                ", house='" + house + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                ", nameOfBoss='" + nameOfBoss + '\'' +
                ", patronymicOfBoss='" + patronymicOfBoss + '\'' +
                ", surnameOfBoss='" + surnameOfBoss + '\'' +
                ", timeOfWork='" + timeOfWork + '\'' +
                ", inn='" + inn + '\'' +
                ", kpp='" + kpp + '\'' +
                ", checkingAccount='" + checkingAccount + '\'' +
                ", correspondentAccount='" + correspondentAccount + '\'' +
                ", bankTitle='" + bankTitle + '\'' +
                ", bicBank='" + bicBank + '\'' +
                ", housesDTO=" + housesDTO +
                ", managementCompanyStatusDTO=" + managementCompanyStatusDTO +
                '}';
    }
}
