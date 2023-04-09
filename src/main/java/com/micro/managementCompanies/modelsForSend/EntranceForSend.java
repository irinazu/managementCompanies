package com.micro.managementCompanies.modelsForSend;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class EntranceForSend implements PlaceInHouse{
    private Long id;
    private Integer numberOfEntrance;
    private List<ImageModel> photoOfLift=new ArrayList<>();
    private List<ImageModel> photoOfEntrance=new ArrayList<>();
    private List<RepairWorkForSend> repairWorkForSend=new ArrayList<>();
    public void addRepairWorkForSend(RepairWorkForSend newRW){
        repairWorkForSend.add(newRW);
    }
    public void addPhotoOfEntrance(ImageModel imageModel){
        photoOfEntrance.add(imageModel);
    }
    public void addPhotoOfLift(ImageModel imageModel){
        photoOfLift.add(imageModel);
    }


}
