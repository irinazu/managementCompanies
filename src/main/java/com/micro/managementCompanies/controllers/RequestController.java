package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.Request;
import com.micro.managementCompanies.models.RequestUpdate;
import com.micro.managementCompanies.models.Request_User;
import com.micro.managementCompanies.services.RequestService;
import com.micro.managementCompanies.services.UserService;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/request")
public class RequestController {

    RequestService requestService;
    UserService userService;

    public RequestController(RequestService requestService, UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

    //Создать Заявку
    @PostMapping("createRequest")
    public void createRequest(@RequestBody Request request){
        requestService.createRequest(request);
    }

    //Сама заявка
    @GetMapping("getCertainRequestForUser/{idOfRequest}")
    public Request getCertainRequestForUser(@PathVariable("idOfRequest") Long idOfRequest){
        return requestService.findById(idOfRequest);
    }

    //История заявки
    @GetMapping("getCertainRequestUpdateForUser/{idOfRequest}")
    public List<RequestUpdate> getCertainRequestUpdateForUser(@PathVariable("idOfRequest") Long idOfRequest){
        List<RequestUpdate> requestUpdatesSorted=requestService.findByRequestId(idOfRequest)
                .stream()
                .sorted(Comparator.comparing(RequestUpdate::getDate))
                .collect(Collectors.toList());

        return requestUpdatesSorted;
    }

    //Все заявки по конкретному пользователю
    @GetMapping("getRequestForUser")
    public List<Request> getRequestForUser(){
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Set<Request_User> request_userSet=userService.getUserByName(auth.getName()).getRequest_userSet();
        Set<Request_User> request_userSet=userService.getUserByName("user1").getRequest_userSet();
        List<Request> requests=new ArrayList<>();
        for (Request_User request_user : request_userSet){
            requests.add(request_user.getRequest());
        }
        return requests;
    }
}
