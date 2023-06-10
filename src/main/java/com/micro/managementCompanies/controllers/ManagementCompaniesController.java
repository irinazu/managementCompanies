package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.ManagementCompany;
import com.micro.managementCompanies.models.ManagementCompanyStatus;
import com.micro.managementCompanies.models.UserSystem;
import com.micro.managementCompanies.modelsForSend.ManagementCompanyDTO;
import com.micro.managementCompanies.modelsForSend.ManagementCompanyStatusDTO;
import com.micro.managementCompanies.modelsForSend.UserSystemDTO;
import com.micro.managementCompanies.services.HouseService;
import com.micro.managementCompanies.services.ManagementCompanyService;
import com.micro.managementCompanies.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("managementCompanies")
public class ManagementCompaniesController {

    ManagementCompanyService managementCompanyService;
    UserService userService;
    HouseService houseService;

    public ManagementCompaniesController(ManagementCompanyService managementCompanyService, UserService userService, HouseService houseService) {
        this.managementCompanyService = managementCompanyService;
        this.userService = userService;
        this.houseService = houseService;
    }

    @GetMapping("getAllMC")
    public List<ManagementCompanyDTO> getAllMC(){
        List<ManagementCompanyDTO> managementCompanyDTOS=new ArrayList<>();
        for (ManagementCompany managementCompany:managementCompanyService.findAllManagementCompany()) {
            ManagementCompanyDTO managementCompanyDTO=new ManagementCompanyDTO();
            managementCompanyDTO.setArgs(managementCompany);
            managementCompanyDTOS.add(managementCompanyDTO);
        }
        return managementCompanyDTOS;
    }

    //определенная УК
    @GetMapping("getCertainMC/{idMC}")
    public ManagementCompanyDTO getCertainMC(@PathVariable("idMC") Long idMC){
        ManagementCompanyDTO managementCompanyDTO=new ManagementCompanyDTO();
        managementCompanyDTO.setArgs(managementCompanyService.findManagementCompany(idMC));
        return managementCompanyDTO;
    }


    @GetMapping("getCertainMCByWorkerMC/{idUser}")
    public ManagementCompanyDTO getCertainMCByWorkerMC(@PathVariable("idUser") Long idUser){
        ManagementCompanyDTO managementCompanyDTO=new ManagementCompanyDTO();
        managementCompanyDTO.setArgs(userService.findUserById(idUser).getManagementCompany());
        return managementCompanyDTO;
    }

    //возвращаем все УК владельца
    @GetMapping("getAllMCForHead/{idUser}")
    public List<ManagementCompanyDTO> getAllMCForHead(@PathVariable("idUser") Long idUser){
        UserSystem userSystem=userService.findUserById(idUser);
        List<ManagementCompanyDTO> managementCompanyDTOS=new ArrayList<>();
        for (ManagementCompany managementCompany:userSystem.getManagementCompaniesForHead()) {
            ManagementCompanyDTO managementCompanyDTO=new ManagementCompanyDTO();
            managementCompanyDTO.setArgs(managementCompany);
            managementCompanyDTOS.add(managementCompanyDTO);
        }
        return managementCompanyDTOS;
    }

    //возвращаем всех работников УК
    @GetMapping("getAllWorkersForMC/{idMC}")
    public List<UserSystemDTO> getAllWorkersForMC(@PathVariable("idMC") Long idMC){
        ManagementCompany managementCompany=managementCompanyService.findManagementCompany(idMC);
        List<UserSystemDTO> userSystemDTOS=new ArrayList<>();

        for (UserSystem userSystem:managementCompany.getUserSystems()) {
            UserSystemDTO userSystemDTO=new UserSystemDTO();
            userSystemDTO.setAllArgs(userSystem,0);
            userSystemDTOS.add(userSystemDTO);
        }
        return userSystemDTOS;
    }

    //УК с определенным статусом
    @GetMapping("getMCForStatus/{status}")
    public List<ManagementCompanyDTO> getMCForStatus(@PathVariable("status") Long status){
        List<ManagementCompanyDTO> managementCompanyDTOS=new ArrayList<>();
        for (ManagementCompany managementCompany:managementCompanyService.findAllByManagementCompanyStatusId(status)) {
            ManagementCompanyDTO managementCompanyDTO=new ManagementCompanyDTO();
            managementCompanyDTO.setArgs(managementCompany);
            managementCompanyDTOS.add(managementCompanyDTO);
        }
        return managementCompanyDTOS;
    }

    //все статусы
    @GetMapping("getAllStatusMC")
    public List<ManagementCompanyStatusDTO> getAllStatusMC(){
        List<ManagementCompanyStatusDTO> managementCompanyStatusDTOS=new ArrayList<>();
        for (ManagementCompanyStatus managementCompanyStatus : managementCompanyService.findAllStatus()){
            ManagementCompanyStatusDTO managementCompanyStatusDTO=new ManagementCompanyStatusDTO();
            managementCompanyStatusDTO.setArgs(managementCompanyStatus);
            managementCompanyStatusDTOS.add(managementCompanyStatusDTO);
        }
        return managementCompanyStatusDTOS;
    }

    //обновление статуса
    @PostMapping("updateStatusFroCertainMC/{idMC}/{idStatus}")
    public ManagementCompanyDTO updateStatusFroCertainMC(@PathVariable("idMC") Long idMC,
                                                         @PathVariable("idStatus") Long idStatus,
                                                         @RequestBody String causeRejection){
        ManagementCompany managementCompany=managementCompanyService.findManagementCompany(idMC);
        ManagementCompanyStatus managementCompanyStatus=managementCompanyService.findByIdStatus(idStatus);
        managementCompany.setManagementCompanyStatus(managementCompanyStatus);
        if(idStatus==3L){
            managementCompany.setCauseRejection(causeRejection);
        }else {
            managementCompany.setCauseRejection(null);
        }
        managementCompany=managementCompanyService.saveManagementCompany(managementCompany);

        ManagementCompanyDTO managementCompanyDTO=new ManagementCompanyDTO();
        managementCompanyDTO.setArgs(managementCompany);
        return managementCompanyDTO;
    }

    //все статусы
    @PostMapping("addMC/{idHead}")
    public void addMC(@PathVariable("idHead") Long idHead,
                      @RequestBody ManagementCompanyDTO managementCompanyDTO){
        ManagementCompany managementCompany=new ManagementCompany();
        managementCompany.setArgs(managementCompanyDTO);
        managementCompany.setHead(userService.findUserById(idHead));
        managementCompany.setManagementCompanyStatus(managementCompanyService.findByIdStatus(1L));
        managementCompanyService.saveManagementCompany(managementCompany);

    }


    //все статусы
    @GetMapping("getMCByHouse/{idHouse}")
    public ManagementCompanyDTO getMCByHouse(@PathVariable("idHouse") Long idHouse){
        ManagementCompanyDTO managementCompany=new ManagementCompanyDTO();
        managementCompany.setArgs(houseService.findHouseById(idHouse).getManagementCompany());
        return managementCompany;
    }

}
