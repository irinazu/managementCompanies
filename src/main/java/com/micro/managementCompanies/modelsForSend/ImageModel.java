package com.micro.managementCompanies.modelsForSend;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class ImageModel {
    Long id;
    public byte[] picBytes;

}
