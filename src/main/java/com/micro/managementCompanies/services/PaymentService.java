package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.Service_User_Pay;
import com.micro.managementCompanies.models.UserSystem;
import com.micro.managementCompanies.repositories.PaymentsRepository;
import com.micro.managementCompanies.repositories.UserSystemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    PaymentsRepository paymentsRepository;
    UserSystemRepository userSystemRepository;

    public PaymentService(PaymentsRepository paymentsRepository, UserSystemRepository userSystemRepository) {
        this.paymentsRepository = paymentsRepository;
        this.userSystemRepository = userSystemRepository;
    }

    public List<Service_User_Pay> getPaymentForUser(Long userId){
        return paymentsRepository.findAllByUserSystemIdOrderByDateDesc(userId);
    }

}
