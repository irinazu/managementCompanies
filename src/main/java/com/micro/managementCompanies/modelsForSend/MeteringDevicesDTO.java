package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.House_User;
import com.micro.managementCompanies.models.MeteringDevices;
import com.micro.managementCompanies.models.MeteringDevicesType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Component
@Getter
@Setter
public class MeteringDevicesDTO {
    private Long id;
    private String series;
    private Date startDate;
    private Date endDate;
    private Date checkDate;
    MeteringDevicesTypeDTO meteringDevicesType=new MeteringDevicesTypeDTO();

    public void setArgs(MeteringDevices meteringDevices) {
        this.id = meteringDevices.getId();
        this.series = meteringDevices.getSeries();
        this.startDate = meteringDevices.getStartDate();
        this.endDate = meteringDevices.getEndDate();
        this.checkDate = meteringDevices.getCheckDate();
        this.meteringDevicesType.setArgs(meteringDevices.getMeteringDevicesType());
    }
}
