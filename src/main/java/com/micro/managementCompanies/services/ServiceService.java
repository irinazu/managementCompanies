package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.House_User;
import com.micro.managementCompanies.models.MeteringDevices;
import com.micro.managementCompanies.models.MeteringDevicesType;
import com.micro.managementCompanies.models.ServiceDescription;
import com.micro.managementCompanies.repositories.MeteringDevicesRepository;
import com.micro.managementCompanies.repositories.MeteringDevicesTypeRepository;
import com.micro.managementCompanies.repositories.ServiceDescriptionRepository;
import com.micro.managementCompanies.repositories.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceService {
    ServiceRepository serviceRepository;
    ServiceDescriptionRepository serviceDescriptionRepository;
    MeteringDevicesRepository meteringDevicesRepository;
    MeteringDevicesTypeRepository meteringDevicesTypeRepository;

    public ServiceService(ServiceRepository serviceRepository, ServiceDescriptionRepository serviceDescriptionRepository, MeteringDevicesRepository meteringDevicesRepository, MeteringDevicesTypeRepository meteringDevicesTypeRepository) {
        this.serviceRepository = serviceRepository;
        this.serviceDescriptionRepository = serviceDescriptionRepository;
        this.meteringDevicesRepository = meteringDevicesRepository;
        this.meteringDevicesTypeRepository = meteringDevicesTypeRepository;
    }

    public com.micro.managementCompanies.models.Service saveService(com.micro.managementCompanies.models.Service service){
        return serviceRepository.save(service);
    }

    public List<com.micro.managementCompanies.models.Service> findFirst12ByServiceDescriptionIdAndUserIdOrderByDateOfConsumption(Long serviceDescriptionId,Long userId){
        return serviceRepository.findFirst12ByServiceDescriptionIdAndUserIdOrderByDateOfConsumption(serviceDescriptionId,userId);
    }

    public List<com.micro.managementCompanies.models.Service> findServicesByYear(Long serviceId,Integer year,Long userId){
        return serviceRepository.findAllByServiceDescriptionIdAndYearAndUserIdOrderByDateOfConsumption(serviceId,year,userId);
    }

    public List<ServiceDescription> getAllDescriptionService(){
        return (List<ServiceDescription>) serviceDescriptionRepository.findAll();
    }

    public ServiceDescription getCertainDescriptionService(Long id){
        return serviceDescriptionRepository.findById(id).get();
    }

    public List<com.micro.managementCompanies.models.Service> getDebtsForService(Long id,Boolean repaid,Long userId){
        return serviceRepository.getDebtsForService(id,repaid,userId);
    }

    public List<Integer> getYears(){
        return serviceRepository.getYears();
    }

    public com.micro.managementCompanies.models.Service getCertainService(Long serviceId,Integer year,Integer monthNumber,Long userId){
        return serviceRepository.findByServiceDescriptionIdAndYearAndMonthNumberAndUserId(serviceId,year,monthNumber,userId);
    }

    //последнюю услугу определенного типа
    public com.micro.managementCompanies.models.Service findLastServiceWihUser(Long serviceDescriptionId,Long userId){
        if(serviceRepository.findFirstByServiceDescriptionIdAndUserIdOrderByDateOfConsumptionDesc(serviceDescriptionId,userId).isPresent()){
            return serviceRepository.findFirstByServiceDescriptionIdAndUserIdOrderByDateOfConsumptionDesc(serviceDescriptionId,userId).get();
        }
        return null;
    }

    //последнюю услугу определенного типа с датой
    public com.micro.managementCompanies.models.Service getAllUsersWithServicesForDate(Long serviceId,Long userId,Integer year,Integer month){
        if(serviceRepository.findFirstByServiceDescriptionIdAndUserIdAndYearAndMonthNumberOrderByDateOfConsumptionDesc(serviceId,userId,year,month).isPresent()){
            return serviceRepository.findFirstByServiceDescriptionIdAndUserIdAndYearAndMonthNumberOrderByDateOfConsumptionDesc(serviceId,userId,year,month).get();
        }
        return null;
    }

    //все услуги определенного типа
    public List<com.micro.managementCompanies.models.Service> findByServiceDescriptionIdAndUserId(Long serviceId,Long userId){
        return serviceRepository.findByServiceDescriptionIdAndUserIdOrderByDateOfConsumptionDesc(serviceId,userId);
    }

    //определенные услуги за определенный год
    public List<com.micro.managementCompanies.models.Service> findByServiceDescriptionIdAndUserIdAndYearOrderByDateOfConsumptionDesc(Long serviceId,Long userId,Integer year){
        return serviceRepository.findByServiceDescriptionIdAndUserIdAndYearOrderByDateOfConsumptionDesc(serviceId,userId,year);
    }

    //определенные услуги за определенный год
    public List<com.micro.managementCompanies.models.Service> findByServiceDescriptionIdAndUserIdAndMonthNumberOrderByDateOfConsumptionDesc(Long serviceId,Long userId,Integer monthNumber){
        return serviceRepository.findByServiceDescriptionIdAndUserIdAndMonthNumberOrderByDateOfConsumptionDesc(serviceId,userId,monthNumber);
    }

    //последнюю ИПУ определенного типа
    public List<MeteringDevicesType> getAllMeteringDeviceType(){
        return (List<MeteringDevicesType>) meteringDevicesTypeRepository.findAll();
    }

    //последнюю ИПУ определенного типа
    public MeteringDevices getLastMeteringDeviceCertainType(Long serviceId, House_User house_user){
        if(meteringDevicesRepository.getLastMeteringDevices(serviceId,house_user).isPresent()){
            return meteringDevicesRepository.getLastMeteringDevices(serviceId,house_user).get();
        }
        return null;
    }

    //определенный тип ипу
    public MeteringDevicesType getMeteringDeviceType(Long serviceIdType){
        return meteringDevicesTypeRepository.findById(serviceIdType).get();
    }

    //сохраняем ипу
    public MeteringDevices saveMeteringDevice(MeteringDevices meteringDevices){
        return meteringDevicesRepository.save(meteringDevices);
    }

    //берем ипу
    public MeteringDevices getCertainMeteringDevice(Long meteringDevicesId){
        return meteringDevicesRepository.findById(meteringDevicesId).get();
    }
}
