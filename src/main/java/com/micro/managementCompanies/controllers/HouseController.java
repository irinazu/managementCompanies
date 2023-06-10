package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.modelsForSend.*;
import com.micro.managementCompanies.services.HouseService;
import com.micro.managementCompanies.services.ManagementCompanyService;
import com.micro.managementCompanies.services.RepairWorkService;
import com.micro.managementCompanies.services.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/")
public class HouseController {
    HouseService houseService;
    RepairWorkService repairWorkService;
    UserService userService;
    ManagementCompanyService managementCompanyService;

    public HouseController(HouseService houseService, RepairWorkService repairWorkService, UserService userService, ManagementCompanyService managementCompanyService) {
        this.houseService = houseService;
        this.repairWorkService = repairWorkService;
        this.userService = userService;
        this.managementCompanyService = managementCompanyService;
    }

    @GetMapping("{region}/{town}/{street}/{house}")
    House getInformationAboutHouse(@PathVariable("region") String region,
                                   @PathVariable("town") String town,
                                   @PathVariable("street") String street,
                                   @PathVariable("house") Long house){
        return houseService.getInformationAboutHouse(region,town,street,house);

    }

    @PostMapping(value = "loadImgOnServer",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    void uploadPhotoInServer(@RequestPart("imgFile")MultipartFile[] files) {
        Arrays.asList(files).stream().forEach(file -> {
            File file2 = new File(FileUtil.folderPath+file.getOriginalFilename());
            try (OutputStream os = new FileOutputStream(file2)) {
                os.write(file.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //метод позже надо комментировать как разберусь полностью с деревом папок
    @GetMapping(value = "getImgFromServer/{titleOfDirectory}/{anotherOne}",produces = MediaType.IMAGE_PNG_VALUE)
    Resource getImgFromServer(@PathVariable("titleOfDirectory") String titleOfDirectory,
                                              @PathVariable("anotherOne") String anotherOne) throws IOException {
        Resource resource=null;
        List<Resource> files=new ArrayList<>();
        File directory=new File(FileUtil.folderPath+titleOfDirectory+"/"+anotherOne);
        for(File file:directory.listFiles()){
            if(file.isFile()){
                Path path = Paths.get(FileUtil.folderPath + titleOfDirectory + "/" + anotherOne + "/" + file.getName());

                /*final Resource inputStream = new Resource(Files.readAllBytes(Paths.get(
                        FileUtil.folderPath + titleOfDirectory + "/" + anotherOne + "/" + file.getName()
                ))) {
                };*/
                resource=new UrlResource(path.toUri());

                //files.add(resource);
            }
        }
        /*final ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(Paths.get(
                FileUtil.folderPath+titleOfDirectory+"/"+anotherOne+"/"+"1.png"
        )));*/

        return resource;
    }

    //метод позже надо удалить как разберусь полностью с деревом папок
    @GetMapping("getFImg/{area}/{town}/{street}/{house}/{certainDirectory}")
    List<ImageModel> getImgReturning(@PathVariable("area") String area,
                                 @PathVariable("town") String town,
                                 @PathVariable("street") String street,
                                 @PathVariable("house") String house,
                                 @PathVariable("certainDirectory") String certainDirectory
                                 ) throws IOException {
        File directory=new File(FileUtil.folderPath +"/"+ "house"+"/"+area+"-"+town+"-"+street+"-"+
                house+"/"+certainDirectory);
        //ImageModel[] imageModels=new ImageModel[directory.listFiles().length];
        List<ImageModel> imageModels=new ArrayList<>();
        int i=0;
        for(File file:directory.listFiles()) {
            if (file.isFile()) {

                // file to byte[], Path
                byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                ImageModel imageModel=new ImageModel();
                imageModel.picBytes=bytes;
                //imageModels[i]=imageModel;
                imageModels.add(imageModel);
                ++i;
                // file to byte[], File -> Path
               /*File file = new File(filePath);
                byte[] bytes = Files.readAllBytes(file.toPath());*/
            }
        }
        return imageModels;
    }

    @GetMapping("getImg/{area}/{town}/{street}/{house}")
    HouseForSend getWholeHouse(@PathVariable("area") String area,
                                 @PathVariable("town") String town,
                                 @PathVariable("street") String street,
                                 @PathVariable("house") Long house
    ) throws IOException {
        House houseFromDB=houseService.getInformationAboutHouse(area,town,street,house);
        HouseForSend houseForSend=new HouseForSend();
        houseForSend.setAllArgs(houseFromDB.getId(),houseFromDB.getRegion(),houseFromDB.getTown(),houseFromDB.getStreet(),houseFromDB.getNumberOfHouse(),
                houseFromDB.getNumberOfEntrance(), houseFromDB.getNumberOfFloor(), houseFromDB.getLift(), houseFromDB.getYearOfConstruction(),
                houseFromDB.getNumberOfFlats(), houseFromDB.getHomeCondition(), houseFromDB.getProblems(), houseFromDB.getAdjoiningTerritory());
        File directoryHouse=new File(FileUtil.folderPath +"/"+ "house"+"/"+area+"-"+town+"-"+street+"-"+ house);
        List<ImageModel> imageModels=new ArrayList<>();
        for(File file: Objects.requireNonNull(directoryHouse.listFiles())) {
            if (file.getName().equals("theHouseItself")&& Objects.requireNonNull(file.listFiles()).length!=0) {
                for (File imgFile : Objects.requireNonNull(file.listFiles())) {
                    byte[] bytes = Files.readAllBytes(Paths.get(imgFile.getAbsolutePath()));
                    ImageModel imageModel = new ImageModel();
                    imageModel.picBytes = bytes;
                    imageModels.add(imageModel);
                }
            }
        }
        houseForSend.setTheHouseItself(imageModels);

        //Roof and Basement and Entrances

        for(File file: Objects.requireNonNull(directoryHouse.listFiles())) {
            if (file.getName().equals("roof")) {
                houseForSend.setRoofForSend((RoofForSend) getBlock(file,"roof"));
            }
            else if (file.getName().equals("basement")) {
                houseForSend.setBasementForSend((BasementForSend) getBlock(file,"basement"));
            }
             if(file.getName().equals("entrances")){
                for(File certainEntrance: Objects.requireNonNull(file.listFiles())) {
                    houseForSend.addEntranceForSend((EntranceForSend) getBlock(certainEntrance,"entrances"));
                    houseForSend.getEntranceForSend().get(houseForSend.getEntranceForSend().size()-1).setNumberOfEntrance(Integer.valueOf(certainEntrance.getName()));
                    //мысл в том чтобы зациклиться на папках одного подъезда, в поисках itself
                    for(File fileRepairWork: Objects.requireNonNull(certainEntrance.listFiles())) {
                        if (fileRepairWork.getName().equals("theEntranceItself")) {
                            List<ImageModel> imageModelTheEntranceItself = new ArrayList<>();
                            for (File imgItself : Objects.requireNonNull(fileRepairWork.listFiles())) {
                                byte[] bytes = Files.readAllBytes(Paths.get(imgItself.getAbsolutePath()));
                                ImageModel imageModel = new ImageModel();
                                imageModel.picBytes = bytes;
                                imageModelTheEntranceItself.add(imageModel);
                            }
                            houseForSend.getEntranceForSend().get(houseForSend.getEntranceForSend().size()-1).setPhotoOfEntrance(imageModelTheEntranceItself);
                        }
                    }
                }
            }
        }
        return houseForSend;
    }

    PlaceInHouse getBlock(File file,String placeInHouse) throws IOException {
        PlaceInHouse blockForSend=null;
        if(placeInHouse.equals("roof")){
            blockForSend=new RoofForSend();
        }else if(placeInHouse.equals("basement")){
            blockForSend=new BasementForSend();
        }else if(placeInHouse.equals("entrances")){
            blockForSend=new EntranceForSend();
        }

        for(File fileRepairWork: Objects.requireNonNull(file.listFiles())){
            if(!fileRepairWork.getName().equals("theEntranceItself")){
                RepairWorkForSend repairWorkForSend=new RepairWorkForSend();
                blockForSend.addRepairWorkForSend(repairWorkForSend);
                String path=fileRepairWork.getPath();
                RepairWork repairWorkFromBD=repairWorkService.getRepairWork(path+"\\after");
                repairWorkForSend.setDate(repairWorkFromBD.getDate());
                repairWorkForSend.setCompany(repairWorkFromBD.getCompany());
                repairWorkForSend.setDescription(repairWorkFromBD.getDescription());
                for (File label: Objects.requireNonNull(fileRepairWork.listFiles())){
                        List<ImageModel> imageModelsBefore=new ArrayList<>();
                        List<ImageModel> imageModelsAfter=new ArrayList<>();
                        if(label.getName().equals("before")){
                            for (File beforeImage: Objects.requireNonNull(label.listFiles())){
                                byte[] bytes = Files.readAllBytes(Paths.get(beforeImage.getAbsolutePath()));
                                ImageModel imageModel=new ImageModel();
                                imageModel.picBytes=bytes;
                                imageModelsBefore.add(imageModel);
                            }
                            repairWorkForSend.setPhotoBefore(imageModelsBefore);
                        }else if(label.getName().equals("after")){
                            for (File afterImage: Objects.requireNonNull(label.listFiles())){
                                byte[] bytes = Files.readAllBytes(Paths.get(afterImage.getAbsolutePath()));
                                ImageModel imageModel=new ImageModel();
                                imageModel.picBytes=bytes;
                                imageModelsAfter.add(imageModel);
                            }
                            repairWorkForSend.setPhotoAfter(imageModelsAfter);
                        }
                    }
                }
            }
        return blockForSend;
        }



    /*    @GetMapping("getImg/{area}/{town}/{street}/{house}")
    HouseForSend getWholeHouse(@PathVariable("area") String area,
                                 @PathVariable("town") String town,
                                 @PathVariable("street") String street,
                                 @PathVariable("house") Long house
    ) throws IOException {
        House houseFromDB=houseService.getInformationAboutHouse(area,town,street,house);
        HouseForSend houseForSend=new HouseForSend();
        houseForSend.setAllArgs(houseFromDB.getId(),houseFromDB.getRegion(),houseFromDB.getTown(),houseFromDB.getStreet(),houseFromDB.getNumberOfHouse(),
                houseFromDB.getNumberOfEntrance(), houseFromDB.getNumberOfFloor(), houseFromDB.getLift(), houseFromDB.getYearOfConstruction(),
                houseFromDB.getNumberOfFlats(), houseFromDB.getHomeCondition(), houseFromDB.getProblems(), houseFromDB.getAdjoiningTerritory());
        File directoryHouse=new File(FileUtil.folderPath +"/"+ "house"+"/"+area+"-"+town+"-"+street+"-"+
                house);
        List<ImageModel> imageModels=new ArrayList<>();
        for(File file:directoryHouse.listFiles()) {
            if (file.getName().equals("theHouseItself")) {
                for (File imgFile : file.listFiles()) {
                    byte[] bytes = Files.readAllBytes(Paths.get(imgFile.getAbsolutePath()));
                    ImageModel imageModel = new ImageModel();
                    imageModel.picBytes = bytes;
                    imageModels.add(imageModel);
                }
            }
        }
        houseForSend.setTheHouseItself(imageModels);

        //Roof and Basement and Entrances
        RoofForSend roofForSend=new RoofForSend();
        BasementForSend basementForSend=new BasementForSend();
        houseForSend.setRoofForSend(roofForSend);
        houseForSend.setBasementForSend(basementForSend);

        for(File file:directoryHouse.listFiles()) {
            if (file.getName().equals("roof")) {
                for(File fileRepairWork:file.listFiles()){
                    RepairWorkForSend repairWorkForSend=new RepairWorkForSend();
                    roofForSend.add(repairWorkForSend);
                   for (File label:fileRepairWork.listFiles()){
                       List<ImageModel> imageModelsBefore=new ArrayList<>();
                       List<ImageModel> imageModelsAfter=new ArrayList<>();
                       if(label.getName().equals("before")){
                           for (File beforeImage:label.listFiles()){
                               byte[] bytes = Files.readAllBytes(Paths.get(beforeImage.getAbsolutePath()));
                               ImageModel imageModel=new ImageModel();
                               imageModel.picBytes=bytes;
                               imageModelsBefore.add(imageModel);

                           }
                           repairWorkForSend.setPhotoBefore(imageModelsBefore);
                       }else if(label.getName().equals("after")){
                           for (File afterImage:label.listFiles()){
                               byte[] bytes = Files.readAllBytes(Paths.get(afterImage.getAbsolutePath()));
                               ImageModel imageModel=new ImageModel();
                               imageModel.picBytes=bytes;
                               imageModelsAfter.add(imageModel);

                           }
                           repairWorkForSend.setPhotoAfter(imageModelsAfter);

                       }
                   }
                }
            }
            else if (file.getName().equals("basement")) {
                for(File fileRepairWork:file.listFiles()){
                    RepairWorkForSend repairWorkForSend=new RepairWorkForSend();
                    basementForSend.add(repairWorkForSend);
                    for (File label:fileRepairWork.listFiles()){
                        List<ImageModel> imageModelsBefore=new ArrayList<>();
                        List<ImageModel> imageModelsAfter=new ArrayList<>();
                        if(label.getName().equals("before")){
                            for (File beforeImage:label.listFiles()){
                                byte[] bytes = Files.readAllBytes(Paths.get(beforeImage.getAbsolutePath()));
                                ImageModel imageModel=new ImageModel();
                                imageModel.picBytes=bytes;
                                imageModelsBefore.add(imageModel);
                            }
                            repairWorkForSend.setPhotoBefore(imageModelsBefore);
                        }else if(label.getName().equals("after")){
                            for (File afterImage:label.listFiles()){
                                byte[] bytes = Files.readAllBytes(Paths.get(afterImage.getAbsolutePath()));
                                ImageModel imageModel=new ImageModel();
                                imageModel.picBytes=bytes;
                                imageModelsAfter.add(imageModel);
                            }
                            repairWorkForSend.setPhotoAfter(imageModelsAfter);
                        }
                    }
                }
            }
             if(file.getName().equals("entrances")){
                for(File certainEntrance:file.listFiles()) {
                    EntranceForSend entranceForSend = new EntranceForSend();
                    entranceForSend.setNumberOfEntrance(Integer.valueOf(certainEntrance.getName()));
                    houseForSend.addEntranceForSend(entranceForSend);
                    for (File fileRepairWork : certainEntrance.listFiles()) {
                        RepairWorkForSend repairWorkForSend = new RepairWorkForSend();
                        entranceForSend.addRepairWorkForSend(repairWorkForSend);
                        if (fileRepairWork.getName().equals("theEntranceItself")) {
                            List<ImageModel> imageModelTheEntranceItself = new ArrayList<>();
                            for (File imgItself : fileRepairWork.listFiles()) {
                                byte[] bytes = Files.readAllBytes(Paths.get(imgItself.getAbsolutePath()));
                                ImageModel imageModel = new ImageModel();
                                imageModel.picBytes = bytes;
                                imageModelTheEntranceItself.add(imageModel);
                            }
                            entranceForSend.setPhotoOfEntrance(imageModelTheEntranceItself);
                        }
                        else {
                            if (!(fileRepairWork.getName().equals("theEntranceItself"))) {
                                for (File label : fileRepairWork.listFiles()) {
                                    List<ImageModel> imageModelsBefore = new ArrayList<>();
                                    List<ImageModel> imageModelsAfter = new ArrayList<>();
                                    if (label.getName().equals("before")) {
                                        for (File beforeImage : label.listFiles()) {
                                            byte[] bytes = Files.readAllBytes(Paths.get(beforeImage.getAbsolutePath()));
                                            ImageModel imageModel = new ImageModel();
                                            imageModel.picBytes = bytes;
                                            imageModelsBefore.add(imageModel);
                                        }
                                        repairWorkForSend.setPhotoBefore(imageModelsBefore);
                                    } else if (label.getName().equals("after")) {
                                        for (File afterImage : label.listFiles()) {
                                            byte[] bytes = Files.readAllBytes(Paths.get(afterImage.getAbsolutePath()));
                                            ImageModel imageModel = new ImageModel();
                                            imageModel.picBytes = bytes;
                                            imageModelsAfter.add(imageModel);
                                        }
                                        repairWorkForSend.setPhotoAfter(imageModelsAfter);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return houseForSend;
    }*/

    //дома для УК определенного работника
    @GetMapping("getHousesForMC/{idUser}/{idMC}")
    public List<HouseForSend> getHousesForMC(@PathVariable("idUser")Long idUser,
                                             @PathVariable("idMC")Long idMC){
        UserSystem userSystem=userService.findUserById(idUser);
        ManagementCompany managementCompany;

        if(userSystem.getRole().getTitle().equals("DISPATCHER")||userSystem.getRole().getTitle().equals("ACCOUNTANT")){
            managementCompany=userService.findUserById(idUser).getManagementCompany();
        }else {
            managementCompany=managementCompanyService.findManagementCompany(idMC);
        }
        List<House> houses=managementCompany.getHouses();
        List<HouseForSend> houseForSends=new ArrayList<>();
        for (House house:houses) {
            HouseForSend houseForSend=new HouseForSend();
            houseForSend.setAllArgsOnHouse(house);
            houseForSends.add(houseForSend);
        }
        return houseForSends;
    }

    //дома для УК по УК
    @GetMapping("getHousesForMCByIdMC/{idMC}")
    public List<HouseForSend> getHousesForMCByIdMC(@PathVariable("idMC") Long idMC){
        ManagementCompany managementCompany=managementCompanyService.findManagementCompany(idMC);
        List<House> houses=managementCompany.getHouses();
        List<HouseForSend> houseForSends=new ArrayList<>();
        for (House house:houses) {
            HouseForSend houseForSend=new HouseForSend();
            houseForSend.setAllArgsOnHouse(house);
            houseForSends.add(houseForSend);
        }
        return houseForSends;
    }

    //юзеры для определенного дома
    @GetMapping("getUsersForHouse/{houseId}")
    public List<UserSystemDTO> getUsersForHouse(@PathVariable("houseId") Long houseId){
        List<House_User> house_users=houseService.findAllHouse_User(houseId);
        List<UserSystemDTO> userSystemDTOS=new ArrayList<>();

        for (House_User house_user : house_users){
            UserSystemDTO userSystemDTO=new UserSystemDTO();
            userSystemDTO.setAllArgs(house_user.getUserSystem(),house_user.getNumberOfFlat());
            userSystemDTOS.add(userSystemDTO);
        }
        return userSystemDTOS;
    }

    //определенный дом
    @GetMapping("getHouse/{houseId}")
    public HouseForSend getHouse(@PathVariable("houseId") Long houseId){
        HouseForSend houseForSend=new HouseForSend();
        houseForSend.setAllArgsOnHouse( houseService.findHouseById(houseId));
        return houseForSend;
    }

    //добавляем дом
    @PostMapping("addHouse/{idMC}")
    public void addHouse(@PathVariable("idMC") Long idMC,
                         @RequestBody HouseForSend houseForSend){
        House house=new House();
        house.setArgs(houseForSend);
        house.setManagementCompany(managementCompanyService.findManagementCompany(idMC));
        house=houseService.saveHouse(house);
        for (int i=0;i<houseForSend.getNumberOfEntrance();i++){
            Entrance entrance=new Entrance();
            entrance.setNumberOfEntrance(i);
            entrance.setHouse(house);
            houseService.saveEntrance(entrance);
        }
    }

    //обновляем дом //нет работы с подъездами
    @PostMapping("reductionHouse")
    public void reductionHouse(@RequestBody HouseForSend houseForSend){
        House house=houseService.findHouseById(houseForSend.getId());
        house.setArgs(houseForSend);
        houseService.saveHouse(house);
    }

    //определенный дом
    @GetMapping("getHouseForCertainUser/{userID}")
    public HouseForSend getHouseForCertainUser(@PathVariable("userID") Long userID){
        UserSystem userSystem=userService.findUserById(userID);
        HouseForSend houseForSend=new HouseForSend();
        houseForSend.setAllArgsOnHouse( userSystem.getHouse_userSet().get(0).getHouse());
        return houseForSend;
    }

    //определенный House_User
    @GetMapping("getHouseUserByUser/{userID}")
    public House_UserDTO getHouseUserByUser(@PathVariable("userID") Long userID){
       House_User house_user=userService.findUserById(userID).getHouse_userSet().get(0);
       House_UserDTO houseUserDTO=new House_UserDTO();
       houseUserDTO.setArgs(house_user);
       return houseUserDTO;
    }

    @GetMapping("getEntranceForHouse/{houseId}")
    public List<EntranceForSend> getEntranceForHouse(@PathVariable("houseId") Long houseId){
        House house=houseService.findHouseById(houseId);
        List<Entrance> entrances=houseService.getEntrances(house.getId());
        List<EntranceForSend> entranceForSends=new ArrayList<>();

        for (Entrance entrance:entrances) {
            EntranceForSend entranceForSend=new EntranceForSend();
            entranceForSend.setArgs(entrance);
            entranceForSends.add(entranceForSend);
        }
        return entranceForSends;
    }

}
