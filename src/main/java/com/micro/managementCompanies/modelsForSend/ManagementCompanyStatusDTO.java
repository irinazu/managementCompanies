package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.ManagementCompany;
import com.micro.managementCompanies.models.ManagementCompanyStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ManagementCompanyStatusDTO {
    private Long id;
    private String titleOfStatus;
    private String filePath;

    public void setArgs(ManagementCompanyStatus managementCompanyStatus) {
        this.id = managementCompanyStatus.getId();
        this.titleOfStatus = managementCompanyStatus.getTitleOfStatus();
        this.filePath = managementCompanyStatus.getFilePath();
    }

    @Override
    public String toString() {
        return "ManagementCompanyStatusDTO{" +
                "id=" + id +
                ", titleOfStatus='" + titleOfStatus + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
