package com.micro.managementCompanies.modelsForSend;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class RepairWorkForSend {
    Long id;
    String date;
    String description;
    String company;
    List<ImageModel> photoBefore=new ArrayList<>();
    List<ImageModel> photoAfter=new ArrayList<>();
    String kindOfWork;

    public void setPhotoAfter(List<ImageModel> photoAfter) {
        this.photoAfter = photoAfter;
    }


}
