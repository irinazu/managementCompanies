package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.ProviderCompany;
import com.micro.managementCompanies.modelsForSend.ProviderCompanyDTO;
import com.micro.managementCompanies.services.ProviderCompanyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    ProviderCompanyService providerCompanyService;

    public ProviderController(ProviderCompanyService providerCompanyService) {
        this.providerCompanyService = providerCompanyService;
    }

    @GetMapping("getProviderCompanyForHouse")
    public List<ProviderCompany> getProviderCompanyForHouse(){
        return providerCompanyService.getProviderCompanyForHouse();
    }

    @GetMapping("getProviderCompaniesForHouse/{idHouse}")
    public List<ProviderCompanyDTO> getProviderCompaniesForHouse(@PathVariable("idHouse") Long idHouse){
        List<ProviderCompanyDTO> providerCompanyDTOS=new ArrayList<>();
        for (ProviderCompany providerCompany:providerCompanyService.getProviderCompaniesForHouse(idHouse)) {
            ProviderCompanyDTO providerCompanyDTO=new ProviderCompanyDTO();
            providerCompanyDTO.setArgs(providerCompany);
            providerCompanyDTOS.add(providerCompanyDTO);
        }
        return providerCompanyDTOS;
    }
}
