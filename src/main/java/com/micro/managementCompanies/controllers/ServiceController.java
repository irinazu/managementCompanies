package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.Service;
import com.micro.managementCompanies.models.ServiceDescription;
import com.micro.managementCompanies.repositories.ServiceRepository;
import com.micro.managementCompanies.services.ServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceController {
    ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    //Получение данных за последний месяц по услуге
    @GetMapping("getLastDataForCounter/{service}")
    public Service getLastDataForCounter(@PathVariable("service") Long service){
        return serviceService.getLastDataForService(service);
    }

    //Сохранение данных об услуге
    @PostMapping("createNewDataForCounter")
    public void createNewDataForCounter(@RequestBody Service service){
        Service serviceForSave=serviceService.getLastDataForService(service.getServiceDescription().getId());
        service.setTariff(serviceForSave.getTariff());
        service.setUnit(serviceForSave.getUnit());
        //serviceForSave.setMonth();??
        service.setDutyForThisMonth(service.getConsumption()*service.getTariff());
        service.setGeneralDutyForService(serviceForSave.getGeneralDutyForService()+service.getDutyForThisMonth());
        service.setDateOfConsumption(new Date());
        serviceService.saveService(service);
    }

    //Получение всех данных по конкретной услуге
    @GetMapping("getDataForService/{service}")
    public List<Service> getDataForService(@PathVariable("service") Long service){
        return serviceService.getDataForService(service);
    }

    //Получение всех 12 блока данных по конкретной услуге
    @GetMapping("getTopDataForService/{service}")
    public List<Service> getTopDataForService(@PathVariable("service") Long service){
        return serviceService.findTop12ByTitleByOrderByDateOfConsumption(service);
    }

    //Получение всех типов услуг
    @GetMapping("getAllDescriptionService")
    public Iterable<ServiceDescription> getAllDescriptionService(){
        return serviceService.getAllDescriptionService();
    }

    //Берем данные по описанию услуги
    @GetMapping("getCertainDescriptionService/{id}")
    public ServiceDescription getCertainDescriptionService(@PathVariable("id") Long id){
        return serviceService.getCertainDescriptionService(id);
    }

    //Берем данные по долгам улуги
    @GetMapping("getDebtsForService/{id}")
    public List<Service> getDebtsForService(@PathVariable("id") Long id){
        return serviceService.getDebtsForService(id,false);
    }

    //Берем данные по месяцам для общего пункта
    @GetMapping("getAllServicesForDate/{year}/{month}")
    public List<Service> getAllServicesForDate(@PathVariable("year") Integer year,
                                               @PathVariable("month") Integer month){
        List<Service> services=serviceService.getAllServicesForDate(year,month);
        if(services!=null&&services.size()!=0){
            return services;
        }else if(month!=1){
            return services=serviceService.getAllServicesForDate(year,month-1);
        }else {
            return services=serviceService.getAllServicesForDate(year-1,12);
        }
    }

    @GetMapping("getYears")
    public List<Integer> getYears(){
        return serviceService.getYears();
    }
}
