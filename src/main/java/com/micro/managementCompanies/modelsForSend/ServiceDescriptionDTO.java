package com.micro.managementCompanies.modelsForSend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.micro.managementCompanies.models.ProviderCompany;
import com.micro.managementCompanies.models.Service;
import com.micro.managementCompanies.models.ServiceDescription;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Component
@Getter
@Setter
public class ServiceDescriptionDTO {

    private Long id;
    private String title;
    private String imgUrl;
    private String counter;
    private String unit;


    public void setArgs(ServiceDescription serviceDescription) {
        this.id = serviceDescription.getId();
        this.title = serviceDescription.getTitle();
        this.imgUrl = serviceDescription.getImgUrl();
        this.counter = serviceDescription.getCounter();
        this.unit=serviceDescription.getUnit();
    }


}
