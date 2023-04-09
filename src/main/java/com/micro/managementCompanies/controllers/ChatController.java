package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.modelsForSend.ChatDTO;
import com.micro.managementCompanies.modelsForSend.ImageModel;
import com.micro.managementCompanies.services.ChatService;
import com.micro.managementCompanies.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("chat")
public class ChatController {
    ChatService chatService;
    UserService userService;

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @GetMapping("getChatImg/{id}")
    public ImageModel getChatImg(@PathVariable("id") Long id) throws IOException {
        return getImgForCertainChat(chatService.findById(id));
    }

    @GetMapping("getAllChatForUser/{id}")
    public List<ChatDTO> getAllChatForUser(@PathVariable("id") Long id) throws IOException {
        List<ChatDTO> chatDTOS=new ArrayList<>();
        for (Chat chat:chatService.getAllChatForUser(id)) {
            ChatDTO chatDTO=new ChatDTO();
            chatDTO.setAllArgs(getImgForCertainChat(chat),chat.getTitle(),chat.getId());
            chatDTOS.add(chatDTO);
        }
        return chatDTOS;
    }

    @PostMapping(value = "loadImgOfChatOnServer/{id}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ImageModel loadImgOfChatOnServer(@PathVariable("id") Long id,
                               @RequestPart("imgFile") MultipartFile file) throws IOException {
        Chat chat=chatService.findById(id);
        String directiveOfChat=chat.getPathForIMgOfChat();
        File directory=new File(FileUtil.folderPath +"/"+ "chat"+"/"+directiveOfChat);
        try {
            for (File fileForDelete:directory.listFiles()) {
                fileForDelete.delete();
            }
        }catch (Exception ignored){}

        File file2 = new File(FileUtil.folderPath+"/chat/"+directiveOfChat+"/"+file.getOriginalFilename());
        try (OutputStream os = new FileOutputStream(file2)) {
            os.write(file.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getImgForCertainChat(chat);
    }

    @PostMapping(value = "loadPhotoForMessage/{idOfMessage}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public List<ImageModel> loadPhotoForMessage(@PathVariable("idOfMessage") Long id,
                                    @RequestParam("imgFile") List<MultipartFile> files) throws IOException {
        Message message = chatService.findByIdMessage(id);
        UserSystem userSystem=message.getUser_system();
        List<ImageModel> imageModels=new ArrayList<>();
        files.stream().forEach(file -> {
            UUID id1 = UUID.randomUUID();
            NestedFile nestedFile=new NestedFile();
            nestedFile.setMessage(message);
            nestedFile.setPathImg(userSystem.getName()+"/"+id1+file.getOriginalFilename());
            chatService.saveNestedFile(nestedFile);
            File file2 = new File(FileUtil.folderPath+"/"+userSystem.getName()+"/"+id1+file.getOriginalFilename());
            try (OutputStream os = new FileOutputStream(file2)) {
                os.write(file.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                imageModels.add(getImg(userSystem.getName()+"/"+id1+file.getOriginalFilename()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return imageModels;
    }

    @PostMapping("sendNewMessage")
    public Long getAllChatForUser(@RequestBody MessageDTO message){
        Message forSave=new Message(message.getContent(),new Date(),
                chatService.findById(message.getChat_id()),
                userService.findUserById(message.getUser_system_id()));

        return chatService.saveMessage(forSave).getId();
    }

    @GetMapping("getAllMessageForChat/{id}")
    public List<MessageAndUserDTO> getAllMessageForChat(@PathVariable("id") Long id) throws IOException {
       Chat chat=chatService.findById(id);
       Set<Message> messages=chat.getMessages();
       List<MessageAndUserDTO> messageAndUserDTOS=new ArrayList<>();
       for(Message message:messages){
           MessageAndUserDTO messageAndUserDTO=new MessageAndUserDTO();
           List<ImageModel> imageModels=new ArrayList<>();
           for (NestedFile nestedFile:message.getNestedFileSet()){
               ImageModel imageModel=getImg(nestedFile.getPathImg());
               imageModel.setId(nestedFile.getId());
               imageModels.add(imageModel);
           }
           messageAndUserDTO.setAvg(message.getId(),message.getContent(),message.getDate(),message.getUser_system().getId(),message.getChat().getId(),message.getUser_system().getName(),imageModels);
           messageAndUserDTOS.add(messageAndUserDTO);
       }
       return messageAndUserDTOS;
    }

    @PostMapping("updateMessage/{idOfMessage}")
    public void updateMessage(@PathVariable("idOfMessage") Long idOfMessage,
                                  @RequestBody MessageDTO message){
        Message forUpdate=chatService.findByIdMessage(idOfMessage);
        forUpdate.setContent(message.getContent());
        if(message.getListImgInNumber().size()!=0){
            for (Long aLong:message.getListImgInNumber()) {
                chatService.deleteByIdNestedFile(aLong);
            }
            for (Long aLong:message.getListImgInNumber()) {
                for(NestedFile nestedFile:forUpdate.getNestedFileSet()){
                    if(aLong.equals(nestedFile.getId())){
                        File file=new File(FileUtil.folderPath +"/"+ nestedFile.getPathImg());
                        file.delete();
                    }
                }
            }
        }
        chatService.saveMessage(forUpdate);
    }

    @PostMapping("deleteMessage")
    public void updateMessage(@RequestBody List<Long> messagesId){
        for(Long aLong:messagesId){
            System.out.println(aLong);
            Message forUpdate=chatService.findByIdMessage(aLong);
            for(NestedFile nestedFile:forUpdate.getNestedFileSet()){
                File file=new File(FileUtil.folderPath +"/"+ nestedFile.getPathImg());
                file.delete();
            }
            chatService.deleteByIdMessage(aLong);
        }
    }

    ImageModel getImgForCertainChat(Chat chat) throws IOException {
        File directory=new File(FileUtil.folderPath +"/"+ "chat"+"/"+chat.getPathForIMgOfChat());
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
        }catch (NullPointerException ignored){}
        return imageModelAvatar;
    }

    ImageModel getImg(String path) throws IOException {
        File file=new File(FileUtil.folderPath +"/"+ path);
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

    @PostMapping("userCloseChat/{chatId}")
    public void test(@PathVariable("chatId") Long chatId){
        //НАХОДИМ ПРОГРАММНО
        Long userId=1L;

        Chat_User chat_user=chatService.getChat(chatId,userId);
        chat_user.setDate(new Date());
        chatService.saveChatUser(chat_user);
    }

    @GetMapping("getDateForUserInChat/{chatId}")
    public Date getDateForUserInChat(@PathVariable("chatId") Long chatId){
        //НАХОДИМ ПРОГРАММНО
        Long userId=1L;

        Chat_User chat_user=chatService.getChat(chatId,userId);
        return chat_user.getDate();
    }
}
