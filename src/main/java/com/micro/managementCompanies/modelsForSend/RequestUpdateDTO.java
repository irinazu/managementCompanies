package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.controllers.FileUtil;
import com.micro.managementCompanies.models.Request;
import com.micro.managementCompanies.models.RequestStatus;
import com.micro.managementCompanies.models.RequestUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.ManyToOne;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Getter
@Setter
public class RequestUpdateDTO {
    private Long id;
    private Date date;
    private String commentary;
    private String file;
    private Boolean userUpdate;
    private Boolean managementCompanyUpdate;
    private RequestStatusDTO requestStatusDTO;
    private Boolean reopenFlag;
    private Long requestId;

    List<ImageModel> imageModelsForRequestUpdate=new ArrayList<>();


    public void setArgs(RequestUpdate requestUpdate) throws IOException {
        this.id = requestUpdate.getId();
        this.date = requestUpdate.getDate();
        this.commentary = requestUpdate.getCommentary();
        this.file = requestUpdate.getFile();
        this.userUpdate = requestUpdate.getUserUpdate();
        this.managementCompanyUpdate = requestUpdate.getManagementCompanyUpdate();

        if(requestUpdate.getRequestStatus()!=null){
            RequestStatusDTO requestStatusDTO = new RequestStatusDTO();
            requestStatusDTO.setArgs(requestUpdate.getRequestStatus());
            this.requestStatusDTO = requestStatusDTO;
        }

        if(requestUpdate.getFile()!=null){
            File directory=new File(FileUtil.folderPath +"/"+ requestUpdate.getFile());
            for (File file:directory.listFiles()) {
                ImageModel newModel=getImg(file);
                imageModelsForRequestUpdate.add(newModel);

            }
        }
    }

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
        return "RequestUpdateDTO{" +
                "id=" + id +
                ", date=" + date +
                ", commentary='" + commentary + '\'' +
                ", file='" + file + '\'' +
                ", userUpdate=" + userUpdate +
                ", managementCompanyUpdate=" + managementCompanyUpdate +
                ", requestStatusDTO=" + requestStatusDTO +
                ", reopenFlag=" + reopenFlag +
                '}';
    }
}
