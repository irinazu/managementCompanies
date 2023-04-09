package com.micro.managementCompanies.modelsForSend;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class BasementForSend implements PlaceInHouse{
    private List<RepairWorkForSend> repairWorkForSend=new ArrayList<>();
    public void addRepairWorkForSend(RepairWorkForSend newRW){
        repairWorkForSend.add(newRW);
    }
}
