package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.controllers.FileUtil;
import com.micro.managementCompanies.models.Request;
import com.micro.managementCompanies.models.RequestStatus;
import com.micro.managementCompanies.models.RequestUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Getter
@Setter
public class RequestDTO {
    private Long id;
    private String title;
    private String town;
    private String street;
    private String house;
    private String flat;
    private String files;
    private String commentary;
    private String publicRequest;
    private Date date;
    RequestStatusDTO lastStatusDTO=new RequestStatusDTO();
    List<RequestUpdateDTO> requestUpdateDTOS=new ArrayList<>();

    ManagementCompanyDTO managementCompanyDTO=new ManagementCompanyDTO();
    RequestThemeDTO requestThemeDTO=new RequestThemeDTO();

    List<ImageModel> imageModelsForRequest=new ArrayList<>();

    public void setArgs(Request request, RequestStatus requestStatus, List<RequestUpdate> requestUpdates) throws IOException {
        this.id = request.getId();
        this.title = request.getTitle();
        this.town = request.getTown();
        this.street = request.getStreet();
        this.house = request.getHouse();
        this.flat = request.getFlat();
        this.files = request.getFiles();
        this.commentary = request.getCommentary();
        this.publicRequest = request.getPublicRequest();
        this.date = request.getDate();
        if(requestStatus!=null){
            lastStatusDTO.setArgs(requestStatus);
        }
        if(requestUpdates!=null){
            for (RequestUpdate requestUpdate : requestUpdates){
                RequestUpdateDTO requestUpdateDTO=new RequestUpdateDTO();
                requestUpdateDTO.setArgs(requestUpdate);
                requestUpdateDTOS.add(requestUpdateDTO);
            }
        }
        managementCompanyDTO.setArgs(request.getManagementCompanyRequest());
        requestThemeDTO.setArgs(request.getRequestTheme());

        if(request.getFiles()!=null){
            File directory=new File(FileUtil.folderPath +"/"+ request.getFiles());
            for (File file:directory.listFiles()) {
                ImageModel newModel=getImg(file);
                imageModelsForRequest.add(newModel);

            }
        }
    }

    //создаем ImageModel
    ImageModel getImg(File file) throws IOException {
        ImageModel imageModelAvatar=null;
        try {
            if (file.isFile()) {
                // file to byte[], Path
                imageModelAvatar=new ImageModel();
                byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                imageModelAvatar.picBytes=bytes;
            }
        }catch (NullPointerException ignored){}
        return imageModelAvatar;
    }

    @Override
    public String toString() {
        return "RequestDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", town='" + town + '\'' +
                ", street='" + street + '\'' +
                ", house='" + house + '\'' +
                ", flat='" + flat + '\'' +
                ", files='" + files + '\'' +
                ", commentary='" + commentary + '\'' +
                ", publicRequest='" + publicRequest + '\'' +
                ", date=" + date +
                ", managementCompanyDTO=" + managementCompanyDTO +
                ", requestThemeDTO=" + requestThemeDTO +
                '}';
    }
}
