package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.UserSystem;
import com.micro.managementCompanies.modelsForSend.ImageModel;
import com.micro.managementCompanies.modelsForSend.UserSystemDTO;
import com.micro.managementCompanies.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("userInfo")
public class UserSystemController {
    UserService userService;

    public UserSystemController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("getUsersForChat/{id}")
    public List<UserSystemDTO> getUsersForChat(@PathVariable("id") Long id) throws IOException {
        List<UserSystemDTO> userSystemDTOS=new ArrayList<>();
        for (UserSystem user:userService.getUsersForChat(id)) {
            UserSystemDTO userSystemDTO=new UserSystemDTO();
            userSystemDTO.setAllArgs(getAvatarForCertainUser(user),user.getName(),user.getId());
            userSystemDTOS.add(userSystemDTO);
        }

        return userSystemDTOS;
    }

    ImageModel getAvatarForCertainUser(UserSystem userSystem) throws IOException {
        File directory=new File(FileUtil.folderPath +"/"+ "userAvatar"+"/"+userSystem.getPathForAvatarImg());
        ImageModel imageModelAvatar=null;
        try {
            for(File file: directory.listFiles()) {
                if (file.isFile()) {
                    // file to byte[], Path
                    imageModelAvatar=new ImageModel();
                    byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                    imageModelAvatar.picBytes=bytes;
                }
            }
        }catch (NullPointerException ignored){
        }
        return imageModelAvatar;
    }
}
