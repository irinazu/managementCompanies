package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.Service;
import com.micro.managementCompanies.models.Service_User_Pay;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Date;

@Component
@Getter
@Setter
public class Service_User_PayDTO {

    private Date date;
    private Float summa;
    private ServiceDTO service=new ServiceDTO();


    public void setArgs(Service_User_Pay serviceUserPay) {
        this.date = serviceUserPay.getDate();
        this.summa = serviceUserPay.getSumma();
        service.setArgs(serviceUserPay.getService());
    }
}
