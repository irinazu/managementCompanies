package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.modelsForSend.ImageModel;
import com.micro.managementCompanies.modelsForSend.RoleDTO;
import com.micro.managementCompanies.modelsForSend.UserSystemDTO;
import com.micro.managementCompanies.services.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("userInfo")
public class UserSystemController {
    UserService userService;
    RequestService requestService;
    ManagementCompanyService managementCompanyService;
    HouseService houseService;
    ChatService chatService;

    public UserSystemController(UserService userService, RequestService requestService, ManagementCompanyService managementCompanyService, HouseService houseService, ChatService chatService) {
        this.userService = userService;
        this.requestService = requestService;
        this.managementCompanyService = managementCompanyService;
        this.houseService = houseService;
        this.chatService = chatService;
    }

    @GetMapping("getUsersForChat/{id}")
    public List<UserSystemDTO> getUsersForChat(@PathVariable("id") Long id) throws IOException {
        List<UserSystemDTO> userSystemDTOS=new ArrayList<>();
        for (UserSystem user:userService.getUsersForChat(id)) {
            UserSystemDTO userSystemDTO=new UserSystemDTO();
            userSystemDTO.setAllArgs(getAvatarForCertainUser(user),user.getName(),user.getSurname(),user.getId());
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

    @PostMapping("login")
    public UserSystemDTO getUsersForChat(@RequestBody UserSystemDTO userSystemDTO) {
        UserSystem userSystem=userService.findUserByEmail(userSystemDTO.getEmail());
        UserSystemDTO returnUserSystemDTO=new UserSystemDTO();

        if(userSystem!=null){
            if(userSystem.getEmail().equals(userSystemDTO.getEmail())){
                if(userSystem.getPassword().equals(userSystemDTO.getPassword())){
                    returnUserSystemDTO.setAllArgs(userSystem,0);
                    return returnUserSystemDTO;
                }else {
                    returnUserSystemDTO.setErrorPassword("Неправильный пароль");
                    return returnUserSystemDTO;
                }
            }else {
                returnUserSystemDTO.setErrorLogin("Неправильный логин");
                return returnUserSystemDTO;
            }
        }

        return null;
    }

    //находим конкретного пользователя
    @GetMapping("findUser/{id}")
    public UserSystemDTO findUser(@PathVariable("id") Long id) throws IOException {
        UserSystemDTO userSystemDTO=new UserSystemDTO();
        userSystemDTO.setAllArgs(userService.findUserById(id),0);
        userSystemDTO.setImgAvatar(getAvatarForCertainUser(userService.findUserById(id)));
        return userSystemDTO;
    }

    //находим конкретного пользователя по заявлению
    @GetMapping("findUserByRequest/{idRequest}")
    public UserSystemDTO findUserByRequest(@PathVariable("idRequest") Long idRequest){
        UserSystemDTO userSystemDTO=new UserSystemDTO();
        UserSystem userSystem=requestService.findById(idRequest).getRequest_userSet().get(0).getUserSystem();
        userSystemDTO.setAllArgs(userSystem,0);
        return userSystemDTO;
    }

    //создаем работника
    @PostMapping("addWorker/{idMC}/{idPost}")
    public void addWorker(@PathVariable("idMC") Long idMC,
                          @RequestBody UserSystemDTO userSystemDTO,
                          @PathVariable("idPost") Long idPost){
        UserSystem userSystem=new UserSystem();
        userSystem.setArgs(userSystemDTO);

        userSystem.setRole(userService.getRoleById(idPost));

        userSystem.setManagementCompany(managementCompanyService.findManagementCompany(idMC));
        userSystem.setPassword(userService.generatePassword());
        userSystem=userService.saveUser(userSystem);
        userService.informAboutRegistration(userSystem);
    }


    //создаем руководителя
    @PostMapping("addHead")
    public void addHead(@RequestBody UserSystemDTO userSystemDTO){
        UserSystem userSystem=new UserSystem();
        userSystem.setArgs(userSystemDTO);

        userSystem.setRole(userService.getRoleById(4L));

        userSystem.setPassword(userSystemDTO.getPassword());
        userSystem=userService.saveUser(userSystem);
        userService.informAboutRegistration(userSystem);
    }

    //создать User
    @PostMapping("addUser/{idHouse}/{idEntrance}")
    public Boolean addUser(@PathVariable("idHouse") Long idHouse,
                           @PathVariable("idEntrance") Long idEntrance,
                           @RequestBody UserSystemDTO userSystemDTO){
        House house=houseService.findHouseById(idHouse);
        Entrance entrance=houseService.getCertainEntrance(idEntrance);

        if(userService.findExistUserSystem(userSystemDTO.getAccountNumber(),house.getManagementCompany().getId())){
            return false;
        }
        UserSystem userSystem=new UserSystem();
        userSystem.setArgsForUser(userSystemDTO);
        userSystem.setRole(userService.getRoleById(1L));
        userSystem=userService.saveUser(userSystem);

        House_User house_user=new House_User();
        HouseUserKey houseUserKey=new HouseUserKey();
        houseUserKey.setArgs(idHouse,userSystem.getId());
        house_user.setArgs(houseUserKey,userSystemDTO.getNumberOfFlat(),house,userSystem,entrance);
        house_user=houseService.saveHouse_User(house_user);
        System.out.println(house_user.getNumberOfFlat());

        return true;
    }

    //создать User
    @PostMapping("finaleRegistrationUser/{idMC}")
    public Integer finaleRegistrationUser(@PathVariable("idMC") Long idMC,
                                         @RequestBody UserSystemDTO userSystemDTO){

        UserSystem userSystem=userService.findUserByEmail(userSystemDTO.getEmail());
        //ищем такой же логин
        if(userSystem!=null){
            return 1;
        }
        //проверяем лицевой счет
        if(!userService.findExistUserSystem(userSystemDTO.getAccountNumber(), idMC)){
            return 2;
        }

        userSystem=userService.findExistUserSystemReturnUser(userSystemDTO.getAccountNumber(), idMC);
        //проверяем нет ли уже регистрации
        if(userSystem.getEmail()!=null){
            return 3;
        }

        userSystem.setPhone(userSystemDTO.getPhone());
        userSystem.setEmail(userSystemDTO.getEmail());
        userSystem.setPassword(userSystemDTO.getPassword());
        userSystem.setFlagOnTakeNews(userSystemDTO.getFlagOnTakeNews());

        //чаты
        House house=userSystem.getHouse_userSet().get(0).getHouse();
        if(house.getChat()==null){
            Chat chat=new Chat();
            //chat.setHouse(house);
            chat.setTitle(house.getStreet()+" "+house.getNumberOfHouse());
            chat=chatService.saveChat(chat);

            house.setChat(chat);
            houseService.saveHouse(house);

            Chat_User chat_user=new Chat_User();
            ChatUserKey chatUserKey=new ChatUserKey(userSystem.getId(),chat.getId());
            chat_user.setArgs(chatUserKey,chat,userSystem,new Date());
            chatService.saveChatUser(chat_user);

            //chat.addInChat_User(chat_user);

        }else {
            Chat_User chat_user=new Chat_User();
            ChatUserKey chatUserKey=new ChatUserKey(userSystem.getId(),house.getChat().getId());
            chat_user.setArgs(chatUserKey,house.getChat(),userSystem,new Date());
            chatService.saveChatUser(chat_user);
        }

        Entrance entrance=userSystem.getHouse_userSet().get(0).getEntrance();

        if(entrance.getChat()==null){
            Chat chat=new Chat();
            //chat.setEntrance(entrance);
            chat.setTitle(house.getStreet()+" "+house.getNumberOfHouse()+" Подъезд: "+entrance.getNumberOfEntrance());
            chat=chatService.saveChat(chat);

            entrance.setChat(chat);
            houseService.saveEntrance(entrance);

            Chat_User chat_user=new Chat_User();
            ChatUserKey chatUserKey=new ChatUserKey(userSystem.getId(),chat.getId());
            chat_user.setArgs(chatUserKey,chat,userSystem,new Date());
            chatService.saveChatUser(chat_user);

            //chat.addInChat_User(chat_user);

        }else {
            Chat_User chat_user=new Chat_User();
            ChatUserKey chatUserKey=new ChatUserKey(userSystem.getId(),entrance.getChat().getId());
            chat_user.setArgs(chatUserKey,entrance.getChat(),userSystem,new Date());
            chatService.saveChatUser(chat_user);
        }

        userService.saveUser(userSystem);
        userService.informAboutRegistration(userSystem);

        return 0;

    }

    //обновляем работника
    @PostMapping("updateWorker/{idPost}")
    public void updateWorker(@RequestBody UserSystemDTO userSystemDTO,
                             @PathVariable("idPost") Long idPost){
        UserSystem userSystem=userService.findUserById(userSystemDTO.getId());
        userSystem.setRole(userService.getRoleById(idPost));
        userSystem.setArgs(userSystemDTO);
        userService.saveUser(userSystem);
    }


    //обновляем Email
    @PostMapping("changeEmail/{userId}")
    public void changeEmail(@PathVariable("userId")Long userId,
                            @RequestBody String email){
        UserSystem userSystem=userService.findUserById(userId);
        userSystem.setEmail(email);
        userService.saveUser(userSystem);
        userService.informAboutChangeEmail(email);
    }

    //обновляем Phone
    @PostMapping("changePhone/{userId}")
    public void changePhone(@PathVariable("userId")Long userId,
                            @RequestBody String phone){
        UserSystem userSystem=userService.findUserById(userId);
        userSystem.setPhone(phone);
        userService.saveUser(userSystem);
        userService.informAboutChangePhone(userSystem.getEmail(),phone);
    }

    //изменяем фдаг на получение новостной рассылки от УК
    @PostMapping("changeFlagOnTakeNews/{userId}")
    public void changeFlagOnTakeNews(@PathVariable("userId")Long userId,
                                     @RequestBody Boolean flag){
        UserSystem userSystem=userService.findUserById(userId);
        userSystem.setFlagOnTakeNews(flag);
        userService.saveUser(userSystem);
    }

    //запрос на обновление Password
    @PostMapping("confirmChangePassword/{userId}")
    public boolean confirmChangePassword(@PathVariable("userId")Long userId,
                                         @RequestBody String oldPassword){
        UserSystem userSystem=userService.findUserById(userId);
        return userSystem.getPassword().equals(oldPassword);
    }

    //обновление Password
    @PostMapping("changePassword/{userId}")
    public void changePassword(@PathVariable("userId")Long userId,
                               @RequestBody String newPassword){
        UserSystem userSystem=userService.findUserById(userId);
        userSystem.setPassword(newPassword);
        userService.saveUser(userSystem);
        userService.informAboutChangePassword(userSystem.getEmail());
    }

    //Создать аватар
    @PostMapping(value = "changeAvatar/{userId}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ImageModel createRequestImg(@PathVariable("userId") Long userId,
                                 @RequestParam("fileForAvatar") MultipartFile file) throws IOException {
        UserSystem userSystem=userService.findUserById(userId);
        String path=userSystem.getId()+"_"+userSystem.getName();
        userSystem.setPathForAvatarImg(path);
        userSystem=userService.saveUser(userSystem);
        Files.createDirectories(Paths.get(FileUtil.folderPath +"/userAvatar/"+path));
        File directory = new File(FileUtil.folderPath+"/userAvatar/"+path);
        for (File fileForDelete:directory.listFiles()) {
            fileForDelete.delete();
        }
        File file2 = new File(FileUtil.folderPath+"/userAvatar/"+path+"/"+file.getOriginalFilename());
        try (OutputStream os = new FileOutputStream(file2)) {
            os.write(file.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getAvatarForCertainUser(userSystem);

    }

    //находим конкретного пользователя по заявлению
    @GetMapping("getPostsForMC")
    public List<RoleDTO> getPostsForMC(){
        List<RoleDTO> roleDTOS=new ArrayList<>();
        for (Role role:userService.getPostsForMC()) {
            RoleDTO roleDTO=new RoleDTO();
            roleDTO.setArgs(role);
            roleDTOS.add(roleDTO);
        }
        return roleDTOS;
    }
}
