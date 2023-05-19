package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.modelsForSend.*;
import com.micro.managementCompanies.services.ManagementCompanyService;
import com.micro.managementCompanies.services.RequestService;
import com.micro.managementCompanies.services.UserService;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/request")
public class RequestController {

    RequestService requestService;
    UserService userService;
    ManagementCompanyService managementCompanyService;

    public RequestController(RequestService requestService, UserService userService, ManagementCompanyService managementCompanyService) {
        this.requestService = requestService;
        this.userService = userService;
        this.managementCompanyService = managementCompanyService;
    }

    //Создать Заявку //реализованно некоректно с точки зрения множественности домов у одного пользователя
    @PostMapping("createRequest/{idUser}")
    public Long createRequest(@PathVariable("idUser") Long idUser,
                              @RequestBody RequestDTO requestDTO){
        UserSystem userSystem=userService.findUserById(idUser);
        RequestTheme requestTheme=requestService.findTheme(requestDTO.getRequestThemeDTO().getId());
        ManagementCompany managementCompany=userSystem.getHouse_userSet().get(0).getHouse().getManagementCompany();
        Request request=new Request(requestDTO,managementCompany,requestTheme);
        request=requestService.createRequest(request);

        //создаю связь заявки и пользователя
        Request_User request_user=new Request_User();
        RequestUserKey requestUserKey=new RequestUserKey();
        requestUserKey.setRequestId(request.getId());
        //ищем пользователя
        requestUserKey.setUserId(idUser);
        request_user.setArgs(requestUserKey,userSystem,request);
        requestService.createRequestUser(request_user);
        return request.getId();
    }

    //Создать Заявку с изображениями
    @PostMapping(value = "createRequestImg/{idRequest}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void createRequestImg(@PathVariable("idRequest") Long idRequest,
                                 @RequestParam("fileForRequest") List<MultipartFile> files) throws IOException {
        Request request=requestService.findById(idRequest);
        String path="requests/"+request.getId()+"/request";
        request.setFiles(path);
        requestService.createRequest(request);
        Files.createDirectories(Paths.get(FileUtil.folderPath +"/"+path));

        files.stream().forEach(file -> {

            File file2 = new File(FileUtil.folderPath+"/"+path+"/"+file.getOriginalFilename());
            try (OutputStream os = new FileOutputStream(file2)) {
                os.write(file.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //Создать обновление заявки
    @PostMapping("createNewRequestUpdate/{idOfRequest}/{idUser}")
    public RequestUpdateDTO createNewRequestUpdate(@PathVariable("idOfRequest") Long idOfRequest,
                                                   @PathVariable("idUser") Long idUser,
                                                   @RequestBody RequestUpdateDTO requestUpdateDTO) throws IOException {
        UserSystem userSystem=userService.findUserById(idUser);

        if(userSystem.getRole().getTitle().equals("USER")){
            requestUpdateDTO.setManagementCompanyUpdate(false);
            requestUpdateDTO.setUserUpdate(true);
        }else {
            requestUpdateDTO.setManagementCompanyUpdate(true);
            requestUpdateDTO.setUserUpdate(false);
        }

        Request request=requestService.findById(idOfRequest);
        RequestUpdate requestUpdate=new RequestUpdate();
        if(requestUpdateDTO.getRequestStatusDTO()!=null){
            RequestStatus requestStatus=requestService.findRequestStatus(requestUpdateDTO.getRequestStatusDTO().getId());
            requestUpdate.setRequestStatus(requestStatus);
        }
        if(requestUpdateDTO.getReopenFlag()){
            RequestStatus requestStatus=requestService.findRequestStatus(2L);
            requestUpdate.setRequestStatus(requestStatus);
        }
        requestUpdate.setArgs(requestUpdateDTO,request);
        requestUpdate=requestService.createRequestUpdate(requestUpdate);

        RequestUpdateDTO requestUpdateDTOForReturn=new RequestUpdateDTO();
        requestUpdateDTOForReturn.setRequestId(request.getId());
        requestUpdateDTOForReturn.setArgs(requestUpdate);
        return requestUpdateDTOForReturn;
    }

    //Создать обновление заявки с изображениями
    @PostMapping(value = "createNewRequestUpdateImg/{idRequestUpdate}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public RequestUpdateDTO createNewRequestUpdateImg(@PathVariable("idRequestUpdate") Long idRequestUpdate,
                                          @RequestParam("fileForRequestUpdate") List<MultipartFile> files) throws IOException {
        RequestUpdate requestUpdate=requestService.findRequestUpdate(idRequestUpdate);
        String forSave="requests/"+requestUpdate.getRequest().getId()+"/updates/"+requestUpdate.getId();
        requestUpdate.setFile(forSave);
        requestService.createRequestUpdate(requestUpdate);
        Files.createDirectories(Paths.get(FileUtil.folderPath +forSave));

        files.stream().forEach(file -> {

            File file2 = new File(FileUtil.folderPath+forSave+"/"+file.getOriginalFilename());
            try (OutputStream os = new FileOutputStream(file2)) {
                os.write(file.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        RequestUpdateDTO requestUpdateDTO=new RequestUpdateDTO();
        requestUpdateDTO.setArgs(requestUpdate);
        return requestUpdateDTO;
    }


    /*dispatcher*/
    //Все заявки по конкретному пользователю
    @GetMapping("getRequestsForUser/{mode}/{idUser}/{role}")
    public List<RequestDTO> getRequestsForUser(@PathVariable("mode") String mode,
                                               @PathVariable("idUser") Long idUser,
                                               @PathVariable("role") String role) throws IOException {
        UserSystem userSystem=userService.findUserById(idUser);
        List<Request> requests=new ArrayList<>();

        List<RequestDTO> requestDTOS=new ArrayList<>();
        if(role.equals("USER")){
            for (Request_User request_user:userSystem.getRequest_userSet()) {
                requests.add(request_user.getRequest());
            }
        }else {
            ManagementCompany managementCompany=userSystem.getManagementCompany();
            requests=requestService.findAllByManagementCompanyRequest(managementCompany.getId());
        }

        for (Request forSearch : requests){
            RequestUpdate requestUpdate=requestService.findRequestUpdateByRequestId(forSearch.getId());
            RequestDTO requestDTO=new RequestDTO();
            if(mode.equals("true")){

                if(requestUpdate!=null&&requestUpdate.getRequestStatus()!=null&&requestUpdate.getRequestStatus().getId()!=4L&&requestUpdate.getRequestStatus().getId()!=5L){
                    requestDTO.setArgs(forSearch,requestUpdate.getRequestStatus(),null);
                    requestDTOS.add(requestDTO);
                }else if(requestUpdate!=null&&requestUpdate.getRequestStatus()==null){
                    requestDTO.setArgs(forSearch,null,null);
                    requestDTOS.add(requestDTO);
                }
                else if(requestUpdate==null) {
                    requestDTO.setArgs(forSearch,requestService.findRequestStatus(1L),null);
                    requestDTOS.add(requestDTO);
                }

            }else {
                if(requestUpdate!=null&&requestUpdate.getRequestStatus()!=null&&(requestUpdate.getRequestStatus().getId()==4L||requestUpdate.getRequestStatus().getId()==5L)){
                    requestDTO.setArgs(forSearch,requestUpdate.getRequestStatus(),null);
                    requestDTOS.add(requestDTO);
                }
            }
        }

        if(!requestDTOS.isEmpty()){
            return requestDTOS;
        }
        return null;
    }

    //все статусы заявлений
    @GetMapping("getStatuses")
    public List<RequestStatusDTO> getStatuses(){
        List<RequestStatusDTO> requestStatusDTOS=new ArrayList<>();
        for (RequestStatus requestStatus:requestService.findAllStatus()) {
            RequestStatusDTO requestStatusDTO=new RequestStatusDTO();
            requestStatusDTO.setArgs(requestStatus);
            requestStatusDTOS.add(requestStatusDTO);
        }
        return requestStatusDTOS;
    }

    //все темы запросов
    @GetMapping("getAllThemeForRequest")
    public List<RequestThemeDTO> getAllThemeForRequest(){
        List<RequestThemeDTO> requestThemeDTOS=new ArrayList<>();
        for (RequestTheme requestTheme:requestService.findAllTheme()) {
            RequestThemeDTO requestThemeDTO=new RequestThemeDTO();
            requestThemeDTO.setArgs(requestTheme);
            requestThemeDTOS.add(requestThemeDTO);
        }
        return requestThemeDTOS;
    }

    //Сама заявка
    @GetMapping("getCertainRequestForUser/{idOfRequest}")
    public RequestDTO getCertainRequestForUser(@PathVariable("idOfRequest") Long idOfRequest) throws IOException {
        Request request=requestService.findById(idOfRequest);
        RequestDTO requestDTO=new RequestDTO();
        requestDTO.setArgs(request,null,request.getRequestUpdates());
        return requestDTO;
    }

}
