package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.Service_User_Pay;
import com.micro.managementCompanies.services.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("getPaymentForUser")
    public List<Service_User_Pay> getPaymentForUser(){
        return paymentService.getPaymentForUser();
    }

}
