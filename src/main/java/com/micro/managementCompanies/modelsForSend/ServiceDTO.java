package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.Service;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Component
@Getter
@Setter
public class ServiceDTO {
    private Long id;
    private String title;
    private Float tariff;
    private Integer consumption;
    private String month;
    private Date dateOfConsumption;
    private Float dutyForThisMonth;
    private Boolean repaid=true;
    private Float generalDutyForService=0F;
    private String unit;
    private Integer year;
    private Integer monthNumber;
    ServiceDescriptionDTO serviceDescription=new ServiceDescriptionDTO();

    public void setArgs(Service service) {
        this.id = service.getId();
        this.tariff = service.getTariff();
        this.consumption = service.getConsumption();
        this.month = service.getMonth();
        this.dateOfConsumption = service.getDateOfConsumption();
        this.dutyForThisMonth = service.getDutyForThisMonth();
        this.repaid = service.getRepaid();
        this.generalDutyForService = service.getGeneralDutyForService();
        this.unit = service.getUnit();
        this.year = service.getYear();
        this.monthNumber = service.getMonthNumber();
        this.serviceDescription.setArgs(service.getServiceDescription());
    }

}
