package com.micro.managementCompanies.controllers;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.modelsForSend.NewsDTO;
import com.micro.managementCompanies.services.ManagementCompanyService;
import com.micro.managementCompanies.services.NewsService;
import com.micro.managementCompanies.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("news")
public class NewsController {
    NewsService newsService;
    UserService userService;
    ManagementCompanyService managementCompanyService;

    public NewsController(NewsService newsService, UserService userService, ManagementCompanyService managementCompanyService) {
        this.newsService = newsService;
        this.userService = userService;
        this.managementCompanyService = managementCompanyService;
    }

    //берем все тэги
    @GetMapping("getAllTags")
    public List<Tag> getAllTAgs(){
        return newsService.getAllTAgs();
    }

    //создаем новую статью
    @PostMapping("createNews/{idOfWorker}/{idMC}")
    public void createNews(@PathVariable("idOfWorker") Long idOfWorker,
                           @PathVariable("idMC") Long idMC,
                           @RequestBody NewsDTO newsDTO){
        UserSystem userSystem=userService.findUserById(idOfWorker);
        ManagementCompany managementCompany;
        if(idMC!=0){
            managementCompany=managementCompanyService.findManagementCompany(idMC);
        }else {
            managementCompany=userSystem.getManagementCompany();
        }
        News news=new News(newsDTO);
        news.setManagementCompany(managementCompany);
        news.setCreator(userSystem);

        news=newsService.saveNewNews(news);
        for (Tag tag:newsDTO.getTagList()) {
            Tag_News tag_news=new Tag_News();
            tag_news.setNews(news);
            TagNewsKey tagNewsKey=new TagNewsKey();
            tagNewsKey.setNewsId(news.getId());
            if(tag.getId().equals(0L)){
                Tag newTag=new Tag();
                newTag.setTitle(tag.getTitle());
                newTag=newsService.saveNewTag(newTag);
                tag_news.setTag(newTag);
                tagNewsKey.setTagId(newTag.getId());
            }else {
                tag_news.setTag(tag);
                tagNewsKey.setTagId(tag.getId());

            }
            tag_news.setTagNewsKey(tagNewsKey);
            newsService.saveNewTagNews(tag_news);
        }

        newsService.informAboutNews(managementCompany,news);
    }


    @PostMapping("updateNews")
    public void updateNews(@RequestBody NewsDTO newsDTO){
        News news=newsService.getCertainNews(newsDTO.getId());
        news.setArgs(newsDTO);
        newsService.saveNewNews(news);

        for (Tag_News tag_news:news.getTag_newsSet()) {
            newsService.deleteTag_news(tag_news);
        }

        for (Tag tag:newsDTO.getTagList()) {
            Tag_News tag_news=new Tag_News();
            tag_news.setNews(news);
            TagNewsKey tagNewsKey=new TagNewsKey();
            tagNewsKey.setNewsId(news.getId());
            if(tag.getId().equals(0L)){
                Tag newTag=new Tag();
                newTag.setTitle(tag.getTitle());
                newTag=newsService.saveNewTag(newTag);
                tag_news.setTag(newTag);
                tagNewsKey.setTagId(newTag.getId());
            }else {
                tag_news.setTag(tag);
                tagNewsKey.setTagId(tag.getId());

            }
            tag_news.setTagNewsKey(tagNewsKey);
            newsService.saveNewTagNews(tag_news);
        }

    }

    //все статьи
    @GetMapping("getAllNews/{idUser}/{role}/{tagId}")
    public List<NewsDTO> getAllNews(@PathVariable("idUser") Long idUser,
                                    @PathVariable("role") String role,
                                    @PathVariable("tagId") Long tagId){
        UserSystem userSystem=userService.findUserById(idUser);
        List<NewsDTO> newsDTOS=new ArrayList<>();
        List<News> newsForSend=new ArrayList<>();

        if(role.equals("USER")){
            List<House> houses=new ArrayList<>();
            List<House_User> house_users=userSystem.getHouse_userSet();
            for (House_User house_user : house_users){
                houses.add(house_user.getHouse());
            }
            for (House house : houses){
                if(tagId==0){
                    newsForSend.addAll(newsService.findAllByManagementCompanyId(house.getManagementCompany().getId()));
                }else{
                    newsForSend.addAll(newsService.findAllForTaggedNews(tagId,house.getManagementCompany().getId()));
                }
            }

        }else {
            if (tagId==0){
                newsForSend=newsService.findAllByManagementCompanyId(userSystem.getManagementCompany().getId());
            }else {
                newsForSend=newsService.findAllForTaggedNews(tagId,userSystem.getManagementCompany().getId());
            }
        }

        for (News news:newsForSend) {
            NewsDTO newsDTO=new NewsDTO();
            newsDTO.setArgs(news);
            newsDTOS.add(newsDTO);
        }

        return newsDTOS;
    }

    //определенная статья
    @GetMapping("getCertainNews/{newsId}")
    public NewsDTO getCertainNews(@PathVariable("newsId") Long newsId){
        News news=newsService.getCertainNews(newsId);
        NewsDTO newsDTO=new NewsDTO();
        newsDTO.setArgs(news);
        return newsDTO;
    }

    /*dispatcher*/
    @GetMapping("getAllNewsCreatedByWorker/{workerId}/{tagId}")
    public List<NewsDTO> getAllNewsCreatedByWorker(@PathVariable("workerId") Long workerId,
                                                   @PathVariable("tagId") Long tagId){
        List<News> news=new ArrayList<>();
        if(tagId==0){
            news=newsService.findAllByCreatorId(workerId);
        }else {
            news=newsService.findAllByCreatorIdWithTag(tagId,workerId);
        }
        List<NewsDTO> newsDTOS=new ArrayList<>();

        for (News certainNews : news){
            NewsDTO newsDTO=new NewsDTO();
            newsDTO.setArgs(certainNews);
            newsDTOS.add(newsDTO);
        }

        return newsDTOS;
    }

    //все статьи
    @GetMapping("getAllNewsForMC/{idMC}/{tagId}")
    public List<NewsDTO> getAllNewsForMC(@PathVariable("idMC") Long idMC,
                                         @PathVariable("tagId") Long tagId){
        List<NewsDTO> newsDTOS=new ArrayList<>();
        List<News> newsForSend=new ArrayList<>();

        if(tagId==0){
            newsForSend=newsService.findAllByManagementCompanyId(idMC);
        }else {
            newsForSend=newsService.findAllForTaggedNews(tagId,idMC);
        }
        for (News news:newsForSend) {
            NewsDTO newsDTO=new NewsDTO();
            newsDTO.setArgs(news);
            newsDTOS.add(newsDTO);
        }

        return newsDTOS;
    }

}
