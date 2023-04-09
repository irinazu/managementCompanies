package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.House;
import com.micro.managementCompanies.models.RepairWork;
import com.micro.managementCompanies.modelsForSend.*;
import com.micro.managementCompanies.services.HouseService;
import com.micro.managementCompanies.services.RepairWorkService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/")
public class HouseController {
    HouseService houseService;
    RepairWorkService repairWorkService;

    public HouseController(HouseService houseService, RepairWorkService repairWorkService) {
        this.houseService = houseService;
        this.repairWorkService = repairWorkService;
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
}
