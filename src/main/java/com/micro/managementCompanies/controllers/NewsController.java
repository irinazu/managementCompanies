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
    @PostMapping("createNews/{idOfWorker}")
    public void createNews(@PathVariable("idOfWorker") Long idOfWorker,
                           @RequestBody NewsDTO newsDTO){
        UserSystem userSystem=userService.findUserById(idOfWorker);
        ManagementCompany managementCompany=userSystem.getManagementCompany();
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
    @GetMapping("getAllNews/{idUser}/{role}")
    public List<NewsDTO> getAllNews(@PathVariable("idUser") Long idUser,
                                    @PathVariable("role") String role){
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
                newsForSend.addAll(house.getManagementCompany().getNews());
            }

        }else {
            newsForSend=newsService.findAllByManagementCompanyId(userSystem.getManagementCompany().getId());
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
    @GetMapping("getAllNewsCreatedByWorker/{workerId}")
    public List<NewsDTO> getAllNewsCreatedByWorker(@PathVariable("workerId") Long workerId){
        List<News> news=newsService.findAllByCreatorId(workerId);
        List<NewsDTO> newsDTOS=new ArrayList<>();

        for (News certainNews : news){
            NewsDTO newsDTO=new NewsDTO();
            newsDTO.setArgs(certainNews);
            newsDTOS.add(newsDTO);
        }

        return newsDTOS;
    }

    //все статьи
    @GetMapping("getAllNewsForMC/{idMC}")
    public List<NewsDTO> getAllNewsForMC(@PathVariable("idMC") Long idMC){
        List<NewsDTO> newsDTOS=new ArrayList<>();
        for (News news:managementCompanyService.findManagementCompany(idMC).getNews()) {
            NewsDTO newsDTO=new NewsDTO();
            newsDTO.setArgs(news);
            newsDTOS.add(newsDTO);
        }

        return newsDTOS;
    }

}
