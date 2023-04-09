package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.RepairWork;
import com.micro.managementCompanies.repositories.RepairWorkRepository;
import org.springframework.stereotype.Service;

@Service
public class RepairWorkService {
    RepairWorkRepository repairWorkRepository;

    public RepairWorkService(RepairWorkRepository repairWorkRepository) {
        this.repairWorkRepository = repairWorkRepository;
    }

    public RepairWork getRepairWork(String link){
        return repairWorkRepository.findByLinkOfPhotoAfterEquals(link);
    }
}
