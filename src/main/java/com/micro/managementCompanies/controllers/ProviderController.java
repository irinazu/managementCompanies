package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.ProviderCompany;
import com.micro.managementCompanies.services.ProviderCompanyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
