package com.micro.managementCompanies.modelsForSend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.micro.managementCompanies.models.House;
import com.micro.managementCompanies.models.HouseUserKey;
import com.micro.managementCompanies.models.UserSystem;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.stereotype.Component;

import javax.persistence.EmbeddedId;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class Flat_User {
    private Integer numberOfFlat;
    private UserSystemDTO userSystemDTO=new UserSystemDTO();
    private List<ServiceDTO> serviceDTOS=new ArrayList<>();
    private List<MeteringDevicesDTO> meteringDevicesDTOS=new ArrayList<>();
    private ServiceDTO serviceDTOReceipt=new ServiceDTO();


    public void setArgs(Integer numberOfFlat, UserSystem userSystem, List<ServiceDTO> serviceDTOS,List<MeteringDevicesDTO> meteringDevicesDTOS) {
        this.numberOfFlat = numberOfFlat;
        this.serviceDTOS = serviceDTOS;
        userSystemDTO.setAllArgs(userSystem,numberOfFlat);
        this.meteringDevicesDTOS=meteringDevicesDTOS;
    }

    public void setArgsForReceipt(Integer numberOfFlat, UserSystem userSystem, ServiceDTO serviceDTO,List<MeteringDevicesDTO> meteringDevicesDTOS) {
        this.numberOfFlat = numberOfFlat;
        this.serviceDTOReceipt = serviceDTO;
        userSystemDTO.setAllArgs(userSystem,numberOfFlat);
        this.meteringDevicesDTOS=meteringDevicesDTOS;
    }
}
