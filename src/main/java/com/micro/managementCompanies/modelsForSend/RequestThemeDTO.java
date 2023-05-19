package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.RequestTheme;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class RequestThemeDTO {
    private Long id;
    private String titleOfTheme;

    public void setArgs(RequestTheme requestTheme) {
        this.id = requestTheme.getId();
        this.titleOfTheme = requestTheme.getTitleOfTheme();
    }
}
