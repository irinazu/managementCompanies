package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.ProviderCompany;
import com.micro.managementCompanies.repositories.HouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderCompanyService {
    HouseRepository houseRepository;

    public ProviderCompanyService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public List<ProviderCompany> getProviderCompanyForHouse(){
        //БЕРЕМ НЫНЕШНЕГО ПОЛЬЗОВАТЕЛЯ НАХОДИМ ЕГО ДОМ
        Long id=1L;
        return houseRepository.getAllByHouse(id);
    }
}
