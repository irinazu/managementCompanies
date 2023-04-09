package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.ServiceDescription;
import com.micro.managementCompanies.repositories.ServiceDescriptionRepository;
import com.micro.managementCompanies.repositories.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceService {
    ServiceRepository serviceRepository;
    ServiceDescriptionRepository serviceDescriptionRepository;

    public ServiceService(ServiceRepository serviceRepository, ServiceDescriptionRepository serviceDescriptionRepository) {
        this.serviceRepository = serviceRepository;
        this.serviceDescriptionRepository = serviceDescriptionRepository;
    }

    public com.micro.managementCompanies.models.Service getLastDataForService(Long service){
        int size=serviceRepository.getDataForService(service).size();
        return serviceRepository.getDataForService(service).get(size-1);
    }
    public void saveService(com.micro.managementCompanies.models.Service service){
        serviceRepository.save(service);
    }

    public List<com.micro.managementCompanies.models.Service> getDataForService(Long service){
        return serviceRepository.getDataForService(service);
    }

    public List<com.micro.managementCompanies.models.Service> findTop12ByTitleByOrderByDateOfConsumption(Long serviceId){
        return serviceRepository.findFirst12ByServiceDescriptionOrderByDateOfConsumption(serviceDescriptionRepository.findById(serviceId).get());
    }

    public Iterable<ServiceDescription> getAllDescriptionService(){
        return serviceDescriptionRepository.findAll();
    }

    public ServiceDescription getCertainDescriptionService(Long id){
        return serviceDescriptionRepository.findById(id).get();
    }

    public List<com.micro.managementCompanies.models.Service> getDebtsForService(Long id,Boolean repaid){
        return serviceRepository.getDebtsForService(id,repaid);
    }

    public List<com.micro.managementCompanies.models.Service> getAllServicesForDate(Integer year,Integer month){
        return serviceRepository.getAllServicesForDate(year,month);
    }

    public List<Integer> getYears(){
        return serviceRepository.getYears();
    }
}
