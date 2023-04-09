package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.Request;
import com.micro.managementCompanies.models.RequestUpdate;
import com.micro.managementCompanies.repositories.RequestRepository;
import com.micro.managementCompanies.repositories.RequestUpdateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {
    RequestUpdateRepository requestUpdateRepository;
    RequestRepository requestRepository;

    public RequestService(RequestUpdateRepository requestUpdateRepository, RequestRepository requestRepository) {
        this.requestUpdateRepository = requestUpdateRepository;
        this.requestRepository = requestRepository;
    }

    public List<RequestUpdate> findByRequestId(Long idRequest){
        return requestUpdateRepository.findByRequestId(idRequest);
    }

    public void createRequest(Request request){
        requestRepository.save(request);
    }

    public Request findById(Long id){
        return requestRepository.findById(id).get();
    }
}
