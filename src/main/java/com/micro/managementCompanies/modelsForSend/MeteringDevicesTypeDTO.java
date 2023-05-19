package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.MeteringDevices;
import com.micro.managementCompanies.models.MeteringDevicesType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Component
@Getter
@Setter
public class MeteringDevicesTypeDTO {
    private Long id;
    private String title;
    private String pathImg;

    public void setArgs(MeteringDevicesType meteringDevicesType) {
        this.id = meteringDevicesType.getId();
        this.title = meteringDevicesType.getTitle();
        this.pathImg=meteringDevicesType.getPathImg();
    }
}
