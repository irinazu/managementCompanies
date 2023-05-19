package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {
    RequestUpdateRepository requestUpdateRepository;
    RequestRepository requestRepository;
    RequestThemeRepository requestThemeRepository;
    RequestUserRepository requestUserRepository;
    RequestStatusRepository requestStatusRepository;

    public RequestService(RequestUpdateRepository requestUpdateRepository, RequestRepository requestRepository, RequestThemeRepository requestThemeRepository, RequestUserRepository requestUserRepository, RequestStatusRepository requestStatusRepository) {
        this.requestUpdateRepository = requestUpdateRepository;
        this.requestRepository = requestRepository;
        this.requestThemeRepository = requestThemeRepository;
        this.requestUserRepository = requestUserRepository;
        this.requestStatusRepository = requestStatusRepository;
    }

    public List<RequestUpdate> findByRequestId(Long idRequest) {
        return requestUpdateRepository.findByRequestId(idRequest);
    }

    //создать запрос
    public Request createRequest(Request request) {
        return requestRepository.save(request);
    }

    //создать обновление запроса
    public RequestUpdate createRequestUpdate(RequestUpdate requestUpdate) {
        return requestUpdateRepository.save(requestUpdate);
    }

    //создать связь запроса и юзера
    public void createRequestUser(Request_User request_user) {
        requestUserRepository.save(request_user);
    }

    //найти запрос по id
    public Request findById(Long id) {
        return requestRepository.findById(id).get();
    }

    //найти последнее обновление запроса
    public RequestUpdate findRequestUpdateByRequestId(Long id) {
        if (requestUpdateRepository.findFirstByRequestIdOrderByIdDesc(id).isPresent()) {
            return requestUpdateRepository.findFirstByRequestIdOrderByIdDesc(id).get();
        } else {
            return null;
        }
    }

    //все темы заявок
    public List<RequestTheme> findAllTheme() {
        return (List<RequestTheme>) requestThemeRepository.findAll();
    }

    //найти тему заявки
    public RequestTheme findTheme(Long id) {
        return requestThemeRepository.findById(id).get();
    }

    //найти обновление заявки
    public RequestUpdate findRequestUpdate(Long id) {
        return requestUpdateRepository.findById(id).get();
    }

    //найти статус заявки
    public RequestStatus findRequestStatus(Long id) {
        return requestStatusRepository.findById(id).get();
    }

    /*dispatcher*/
    //найти заявки по УК
    public List<Request> findAllByManagementCompanyRequest(Long id) {
        return requestRepository.findAllByManagementCompanyRequestId(id);
    }

    //все статусы
    public List<RequestStatus> findAllStatus() {
        return (List<RequestStatus>) requestStatusRepository.findAll();
    }

}