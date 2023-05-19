package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.modelsForSend.*;
import com.micro.managementCompanies.repositories.ServiceRepository;
import com.micro.managementCompanies.services.HouseService;
import com.micro.managementCompanies.services.ServiceService;
import com.micro.managementCompanies.services.UserService;
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
    HouseService houseService;
    UserService userService;

    public ServiceController(ServiceService serviceService, HouseService houseService, UserService userService) {
        this.serviceService = serviceService;
        this.houseService = houseService;
        this.userService = userService;
    }


    //последний блок данных по услуге
    @GetMapping("getLastDataForCounter/{serviceDescriptionId}/{userId}")
    public ServiceDTO getLastDataForCounter(@PathVariable("serviceDescriptionId") Long serviceDescriptionId,
                                            @PathVariable("userId") Long userId){
        ServiceDTO serviceDTO=new ServiceDTO();
        Service service=serviceService.findLastServiceWihUser(serviceDescriptionId,userId);
        if(service!=null){
            serviceDTO.setArgs(service);
            return serviceDTO;
        }
        return null;
    }

    //Сохранение данных об услуге
    @PostMapping("createNewDataForCounter/{userId}")
    public ServiceDTO createNewDataForCounter(@RequestBody Service service,
                                        @PathVariable("userId") Long userId){
        Service serviceForSave=serviceService.findLastServiceWihUser(service.getServiceDescription().getId(),userId);
        House_User house_user=userService.findUserById(userId).getHouse_userSet().get(0);
        ServiceDescription serviceDescription=serviceService.getCertainDescriptionService(service.getServiceDescription().getId());
        service.setServiceDescription(serviceDescription);
        service.setTariff(serviceForSave.getTariff());
        service.setMonthNumber(new Date().getMonth());
        service.setYear(service.getYear());
        service.setRepaid(false);
        service.setDutyForThisMonth(service.getConsumption()*service.getTariff());
        service.setGeneralDutyForService(serviceForSave.getGeneralDutyForService()+service.getDutyForThisMonth());
        service.setDateOfConsumption(new Date());
        service.setUser(userService.findUserById(userId));
        service.setMeteringDevice(serviceService.getLastMeteringDeviceCertainType(serviceDescription.getId(),house_user));
        service=serviceService.saveService(service);

        ServiceDTO serviceDTO=new ServiceDTO();
        serviceDTO.setArgs(service);
        return serviceDTO;
    }

    //Получение всех данных по конкретной услуге
    @GetMapping("getDataForService/{serviceDescription}/{userId}")
    public List<Flat_User> getDataForService(@PathVariable("serviceDescription") Long serviceDescription,
                                             @PathVariable("userId") Long userId){
        House_User house_user=userService.findUserById(userId).getHouse_userSet().get(0);
        List<Flat_User> flat_users=new ArrayList<>();
        List<Service> services=serviceService.findByServiceDescriptionIdAndUserId(serviceDescription,userId);

        if(services.size()!=0){
            for (Service service : services){
                Flat_User flat_user=new Flat_User();
                List<MeteringDevicesDTO> meteringDevicesDTOS=new ArrayList<>();

                ServiceDTO serviceDTO=new ServiceDTO();
                serviceDTO.setArgs(service);
                if(service.getServiceDescription().getCounter().equals("true")){
                    MeteringDevicesDTO meteringDevicesDTO=new MeteringDevicesDTO();
                    meteringDevicesDTO.setArgs(service.getMeteringDevice());
                    meteringDevicesDTOS.add(meteringDevicesDTO);
                }
                flat_user.setArgsForReceipt(house_user.getNumberOfFlat(),house_user.getUserSystem(),serviceDTO,meteringDevicesDTOS);
                flat_users.add(flat_user);
            }
        }

        return flat_users;
    }

    //Получение всех 12 блоков данных по конкретной услуге
    @GetMapping("getTopDataForService/{serviceDescription}/{year}/{userId}")
    public List<ServiceDTO> getTopDataForService(@PathVariable("serviceDescription") Long serviceDescription,
                                                 @PathVariable("year") Integer year,
                                                 @PathVariable("userId") Long userId){
        List<ServiceDTO> serviceDTOS=new ArrayList<>();
        List<Service> services=new ArrayList<>();

        if(year.equals(0)){
            services=serviceService.findFirst12ByServiceDescriptionIdAndUserIdOrderByDateOfConsumption(serviceDescription,userId);
        }else {
            services=serviceService.findServicesByYear(serviceDescription,year,userId);
        }

        for (Service serviceReturn :services) {
            ServiceDTO serviceDTO=new ServiceDTO();
            serviceDTO.setArgs(serviceReturn);
            serviceDTOS.add(serviceDTO);
        }

        return serviceDTOS;
    }

    //определенные услуги за определенный год
    @GetMapping("getServicesByYear/{serviceDescription}/{year}/{userId}")
    public List<Flat_User> getServicesByYear(@PathVariable("serviceDescription") Long serviceDescription,
                                             @PathVariable("year") Integer year,
                                             @PathVariable("userId") Long userId){

        House_User house_user=userService.findUserById(userId).getHouse_userSet().get(0);
        List<Flat_User> flat_users=new ArrayList<>();
        List<Service> services=serviceService.findByServiceDescriptionIdAndUserIdAndYearOrderByDateOfConsumptionDesc(serviceDescription,userId,year);

        if(services.size()!=0){
            for (Service service : services){
                Flat_User flat_user=new Flat_User();
                List<MeteringDevicesDTO> meteringDevicesDTOS=new ArrayList<>();

                ServiceDTO serviceDTO=new ServiceDTO();
                serviceDTO.setArgs(service);
                if(service.getServiceDescription().getCounter().equals("true")){
                    MeteringDevicesDTO meteringDevicesDTO=new MeteringDevicesDTO();
                    meteringDevicesDTO.setArgs(service.getMeteringDevice());
                    meteringDevicesDTOS.add(meteringDevicesDTO);
                }
                flat_user.setArgsForReceipt(house_user.getNumberOfFlat(),house_user.getUserSystem(),serviceDTO,meteringDevicesDTOS);
                flat_users.add(flat_user);
            }

        }
        return flat_users;
    }

    //определенные услуги за определенный месяц
    @GetMapping("getServicesByMonth/{serviceDescription}/{monthNumber}/{userId}")
    public List<Flat_User> getServicesByMonth(@PathVariable("serviceDescription") Long serviceDescription,
                                             @PathVariable("monthNumber") Integer monthNumber,
                                             @PathVariable("userId") Long userId){

        House_User house_user=userService.findUserById(userId).getHouse_userSet().get(0);
        List<Flat_User> flat_users=new ArrayList<>();
        List<Service> services=serviceService.findByServiceDescriptionIdAndUserIdAndMonthNumberOrderByDateOfConsumptionDesc(serviceDescription,userId,monthNumber);
        if(services.size()!=0){
            for (Service service : services){
                Flat_User flat_user=new Flat_User();
                List<MeteringDevicesDTO> meteringDevicesDTOS=new ArrayList<>();

                ServiceDTO serviceDTO=new ServiceDTO();
                serviceDTO.setArgs(service);
                if(service.getServiceDescription().getCounter().equals("true")){
                    MeteringDevicesDTO meteringDevicesDTO=new MeteringDevicesDTO();
                    meteringDevicesDTO.setArgs(service.getMeteringDevice());
                    meteringDevicesDTOS.add(meteringDevicesDTO);
                }
                flat_user.setArgsForReceipt(house_user.getNumberOfFlat(),house_user.getUserSystem(),serviceDTO,meteringDevicesDTOS);
                flat_users.add(flat_user);
            }
        }
        return flat_users;
    }


    //Получение всех типов услуг
    @GetMapping("getAllDescriptionService")
    public List<ServiceDescriptionDTO> getAllDescriptionService(){
        List<ServiceDescriptionDTO> serviceDescriptionDTOS=new ArrayList<>();
        for (ServiceDescription serviceDescription:serviceService.getAllDescriptionService()) {
            ServiceDescriptionDTO serviceDescriptionDTO=new ServiceDescriptionDTO();
            serviceDescriptionDTO.setArgs(serviceDescription);
            serviceDescriptionDTOS.add(serviceDescriptionDTO);
        }
        return serviceDescriptionDTOS;
    }

    //Берем данные по описанию услуги
    @GetMapping("getCertainDescriptionService/{id}")
    public ServiceDescriptionDTO getCertainDescriptionService(@PathVariable("id") Long id){
        ServiceDescriptionDTO serviceDescriptionDTO=new ServiceDescriptionDTO();
        ServiceDescription serviceDescription=serviceService.getCertainDescriptionService(id);
        serviceDescriptionDTO.setArgs(serviceDescription);
        return serviceDescriptionDTO;
    }

    //Берем данные по долгам улуги
    @GetMapping("getDebtsForService/{serviceDescriptionId}/{userID}")
    public List<Flat_User> getDebtsForService(@PathVariable("serviceDescriptionId") Long serviceDescriptionId,
                                            @PathVariable("userID") Long userId){
        List<Service> services=serviceService.getDebtsForService(serviceDescriptionId,false,userId);

        House_User house_user=userService.findUserById(userId).getHouse_userSet().get(0);
        List<Flat_User> flat_users=new ArrayList<>();

        if(services.size()!=0){
            for (Service service : services){
                Flat_User flat_user=new Flat_User();
                List<MeteringDevicesDTO> meteringDevicesDTOS=new ArrayList<>();

                ServiceDTO serviceDTO=new ServiceDTO();
                serviceDTO.setArgs(service);
                if(service.getServiceDescription().getCounter().equals("true")){
                    MeteringDevicesDTO meteringDevicesDTO=new MeteringDevicesDTO();
                    meteringDevicesDTO.setArgs(service.getMeteringDevice());
                    meteringDevicesDTOS.add(meteringDevicesDTO);
                }
                flat_user.setArgsForReceipt(house_user.getNumberOfFlat(),house_user.getUserSystem(),serviceDTO,meteringDevicesDTOS);
                flat_users.add(flat_user);
            }

        }
        return flat_users;
    }

    //Берем данные по месяцам для общего пункта
    @GetMapping("getAllServicesForDate/{year}/{month}/{userId}")
    public Flat_User getAllServicesForDate(@PathVariable("year") Integer year,
                                           @PathVariable("month") Integer month,
                                           @PathVariable("userId") Long userId){
        UserSystem userSystem=userService.findUserById(userId);
        Flat_User flat_user=new Flat_User();

        House_User house_user=userSystem.getHouse_userSet().get(0);
        List<ServiceDescription> serviceDescriptions=serviceService.getAllDescriptionService();

        List<ServiceDTO> serviceDTOS=new ArrayList<>();
        List<MeteringDevicesDTO> meteringDevicesDTOS=new ArrayList<>();

        for (ServiceDescription serviceDescription:serviceDescriptions) {
            Service service=serviceService.getAllUsersWithServicesForDate(serviceDescription.getId(),userId,year,month);
            if(service!=null){
                ServiceDTO serviceDTO=new ServiceDTO();
                serviceDTO.setArgs(service);
                serviceDTOS.add(serviceDTO);
                if(service.getServiceDescription().getCounter().equals("true")){
                    MeteringDevicesDTO meteringDevicesDTO=new MeteringDevicesDTO();
                    meteringDevicesDTO.setArgs(service.getMeteringDevice());
                    meteringDevicesDTOS.add(meteringDevicesDTO);
                }
            }
        }

        flat_user.setArgs(house_user.getNumberOfFlat(),house_user.getUserSystem(),serviceDTOS,meteringDevicesDTOS);

        return flat_user;
    }

    @GetMapping("getYears")
    public List<Integer> getYears(){
        return serviceService.getYears();
    }

    @GetMapping("getCertainService/{id}/{year}/{month}/{userId}")
    public Flat_User getCertainService(@PathVariable("id") Long id,
                                     @PathVariable("year") Integer year,
                                     @PathVariable("month") Integer month,
                                     @PathVariable("userId") Long userId){
        Flat_User flat_user=new Flat_User();
        UserSystem userSystem=userService.findUserById(userId);
        House_User house_user=userSystem.getHouse_userSet().get(0);

        ServiceDTO serviceDTO=new ServiceDTO();
        Service service=serviceService.getCertainService(id,year,month,userId);
        if(service!=null){
            serviceDTO.setArgs(service);

            MeteringDevices meteringDevices=service.getMeteringDevice();
            List<MeteringDevicesDTO> meteringDevicesDTOS=new ArrayList<>();
            MeteringDevicesDTO meteringDevicesDTO=new MeteringDevicesDTO();
            meteringDevicesDTO.setArgs(meteringDevices);
            meteringDevicesDTOS.add(meteringDevicesDTO);
            flat_user.setArgsForReceipt(house_user.getNumberOfFlat(),house_user.getUserSystem(),serviceDTO,meteringDevicesDTOS);

        }else {
            return null;
        }

        return flat_user;
    }

    //долги по дому
    @GetMapping("getGeneralDutyWithDetails/{houseId}")
    public List<Flat_User> getGeneralDutyWithDetails(@PathVariable("houseId") Long houseId){
        List<Flat_User> flat_users=new ArrayList<>();
        List<ServiceDescription> serviceDescriptions=serviceService.getAllDescriptionService();
        House house=houseService.findHouseById(houseId);

        List<House_User> house_users=house.getHouse_userSet();
        for (House_User house_user : house_users){
            Flat_User flat_user=new Flat_User();

            List<ServiceDTO> serviceDTOS=new ArrayList<>();
            for (ServiceDescription serviceDescription:serviceDescriptions) {
                Service service=serviceService.findLastServiceWihUser(serviceDescription.getId(),house_user.getUserSystem().getId());
                if(service!=null&&!service.getRepaid()){
                    ServiceDTO serviceDTO=new ServiceDTO();
                    serviceDTO.setArgs(service);
                    serviceDTOS.add(serviceDTO);
                }
            }
            if(serviceDTOS.size()!=0){
                flat_user.setArgs(house_user.getNumberOfFlat(),house_user.getUserSystem(),serviceDTOS,null);
                flat_users.add(flat_user);
            }
        }

        return flat_users;
    }

    //индивидуальные приборы учета по дому
    @GetMapping("getMeteringDevices/{houseId}")
    public List<Flat_User> getMeteringDevices(@PathVariable("houseId") Long houseId){
        List<Flat_User> flat_users=new ArrayList<>();
        House house=houseService.findHouseById(houseId);

        List<House_User> house_users=house.getHouse_userSet();
        for (House_User house_user : house_users){
            Flat_User flat_user=new Flat_User();
            List<MeteringDevicesDTO> meteringDevicesDTOS=new ArrayList<>();
            for (MeteringDevices meteringDevices:house_user.getMeteringDevices()) {
                MeteringDevicesDTO meteringDevicesDTO=new MeteringDevicesDTO();
                meteringDevicesDTO.setArgs(meteringDevices);
                meteringDevicesDTOS.add(meteringDevicesDTO);
            }
            if(meteringDevicesDTOS.size()!=0){
                flat_user.setArgs(house_user.getNumberOfFlat(),house_user.getUserSystem(),null,meteringDevicesDTOS);
                flat_users.add(flat_user);
            }
        }

        return flat_users;
    }


    //квитанции по дому и дате
    @GetMapping("getAllUsersWithServicesForDate/{houseId}/{year}/{month}")
    public List<Flat_User> getAllUsersWithServicesForDate(@PathVariable("houseId") Long houseId,
                                                          @PathVariable("year") Integer year,
                                                          @PathVariable("month") Integer month){
        List<Flat_User> flat_users=new ArrayList<>();
        List<ServiceDescription> serviceDescriptions=serviceService.getAllDescriptionService();
        House house=houseService.findHouseById(houseId);

        List<House_User> house_users=house.getHouse_userSet();
        for (House_User house_user : house_users){
            Flat_User flat_user=new Flat_User();

            List<ServiceDTO> serviceDTOS=new ArrayList<>();
            for (ServiceDescription serviceDescription:serviceDescriptions) {
                Service service=serviceService.getAllUsersWithServicesForDate(serviceDescription.getId(),house_user.getUserSystem().getId(),year,month);
                if(service!=null){
                    ServiceDTO serviceDTO=new ServiceDTO();
                    serviceDTO.setArgs(service);
                    serviceDTOS.add(serviceDTO);
                }
            }
            List<MeteringDevicesDTO> meteringDevicesDTOS=new ArrayList<>();
            for (MeteringDevices meteringDevices:house_user.getMeteringDevices()) {
                MeteringDevicesDTO meteringDevicesDTO=new MeteringDevicesDTO();
                meteringDevicesDTO.setArgs(meteringDevices);
                meteringDevicesDTOS.add(meteringDevicesDTO);
            }

            if(serviceDTOS.size()!=0){
                flat_user.setArgs(house_user.getNumberOfFlat(),house_user.getUserSystem(),serviceDTOS,meteringDevicesDTOS);
                flat_users.add(flat_user);
            }

        }

        return flat_users;
    }

    //самые последние ипу
    @GetMapping("getMeteringDevicesForUser/{userId}")
    public List<MeteringDevicesDTO> getMeteringDevicesForUser(@PathVariable("userId") Long userId){

        UserSystem userSystem=userService.findUserById(userId);
        House_User house_user=userSystem.getHouse_userSet().get(0);
        List<MeteringDevicesType> meteringDevicesTypes=serviceService.getAllMeteringDeviceType();
        List<MeteringDevicesDTO> meteringDevicesDTOS=new ArrayList<>();

        for (MeteringDevicesType devicesType : meteringDevicesTypes){
           MeteringDevices meteringDevice=serviceService.getLastMeteringDeviceCertainType(devicesType.getId(),house_user);
           MeteringDevicesDTO meteringDevicesDTO=new MeteringDevicesDTO();

           if(meteringDevice!=null){
               meteringDevicesDTO.setArgs(meteringDevice);
           }else {
                MeteringDevicesTypeDTO meteringDevicesTypeDTO=new MeteringDevicesTypeDTO();
                meteringDevicesTypeDTO.setArgs(devicesType);
                meteringDevicesDTO.setMeteringDevicesType(meteringDevicesTypeDTO);
           }
            meteringDevicesDTOS.add(meteringDevicesDTO);

        }

        return meteringDevicesDTOS;

    }

    //добавляем ипу
    @PostMapping("addMeteringDevice/{userId}/{meteringDevicesIdType}")
    public void addMeteringDevice(@RequestBody MeteringDevicesDTO meteringDevicesDTO,
                                  @PathVariable("userId") Long userId,
                                  @PathVariable("meteringDevicesIdType") Long meteringDevicesIdType){

        MeteringDevices meteringDevices=new MeteringDevices();
        meteringDevices.setArgs(meteringDevicesDTO);
        meteringDevices.setMeteringDevicesType(serviceService.getMeteringDeviceType(meteringDevicesIdType));
        House_User house_user=userService.findUserById(userId).getHouse_userSet().get(0);
        meteringDevices.setHouse_user(house_user);

        serviceService.saveMeteringDevice(meteringDevices);

    }

    //определенный ипу
    @GetMapping("getCertainMeteringDevice/{idMeteringDevice}")
    public MeteringDevicesDTO getCertainMeteringDevice(@PathVariable("idMeteringDevice") Long idMeteringDevice){
        MeteringDevicesDTO meteringDevicesDTO=new MeteringDevicesDTO();
        meteringDevicesDTO.setArgs(serviceService.getCertainMeteringDevice(idMeteringDevice));
        return meteringDevicesDTO;
    }

    //обновляем ипу
    @PostMapping("updateMeteringDevice")
    public void updateMeteringDevice(@RequestBody MeteringDevicesDTO meteringDevicesDTO){

        MeteringDevices meteringDevices=serviceService.getCertainMeteringDevice(meteringDevicesDTO.getId());
        meteringDevices.setArgs(meteringDevicesDTO);

        serviceService.saveMeteringDevice(meteringDevices);

    }

    @GetMapping("getCertainMeteringDeviceType/{userTypeId}")
    public MeteringDevicesTypeDTO getCertainMeteringDeviceType(@PathVariable("userTypeId") Long userTypeId){
        MeteringDevicesTypeDTO meteringDevicesTypeDTO=new MeteringDevicesTypeDTO();
        meteringDevicesTypeDTO.setArgs(serviceService.getMeteringDeviceType(userTypeId));
        return meteringDevicesTypeDTO;
    }

}
