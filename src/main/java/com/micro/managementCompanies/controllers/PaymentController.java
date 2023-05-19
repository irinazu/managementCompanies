package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.Service_User_Pay;
import com.micro.managementCompanies.modelsForSend.Service_User_PayDTO;
import com.micro.managementCompanies.services.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("getPaymentForUser/{userId}")
    public List<Service_User_PayDTO> getPaymentForUser(@PathVariable("userId") Long userId){
        List<Service_User_PayDTO> serviceUserPayDTOS=new ArrayList<>();
        for (Service_User_Pay serviceUserPay:paymentService.getPaymentForUser(userId)) {
            Service_User_PayDTO serviceUserPayDTO=new Service_User_PayDTO();
            serviceUserPayDTO.setArgs(serviceUserPay);
            serviceUserPayDTOS.add(serviceUserPayDTO);
        }
        return serviceUserPayDTOS;
    }

}
