package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.RequestStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class RequestStatusDTO {
    private Long id;
    private String titleOfStatus;
    private String filePath;

    public void setArgs(RequestStatus requestStatus) {
        this.id = requestStatus.getId();
        this.titleOfStatus = requestStatus.getTitleOfStatus();
        this.filePath = requestStatus.getFilePath();
    }
}
