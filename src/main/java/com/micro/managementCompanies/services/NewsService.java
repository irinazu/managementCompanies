package com.micro.managementCompanies.services;

import com.micro.managementCompanies.models.*;
import com.micro.managementCompanies.repositories.NewsRepository;
import com.micro.managementCompanies.repositories.TagNewsRepository;
import com.micro.managementCompanies.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {
    TagRepository tagRepository;
    NewsRepository newsRepository;
    TagNewsRepository tagNewsRepository;
    EmailMailSender emailMailSender;

    public NewsService(TagRepository tagRepository, NewsRepository newsRepository, TagNewsRepository tagNewsRepository, EmailMailSender emailMailSender) {
        this.tagRepository = tagRepository;
        this.newsRepository = newsRepository;
        this.tagNewsRepository = tagNewsRepository;
        this.emailMailSender = emailMailSender;
    }

    public List<Tag> getAllTAgs(){
        return (List<Tag>) tagRepository.findAll();
    }

    public List<News> findAllByManagementCompanyId(Long id){
        return newsRepository.findAllByManagementCompanyIdOrderByDateOfPublicationDesc(id);
    }
    public News getCertainNews(Long id){
        return newsRepository.findById(id).get();
    }

    public Tag saveNewTag(Tag tag){return tagRepository.save(tag);}
    public News saveNewNews(News news){
        return newsRepository.save(news);
    }
    public void saveNewTagNews(Tag_News tag_news){tagNewsRepository.save(tag_news);}

    //все новости по их создателю
    public List<News> findAllByCreatorId(Long id){
        return newsRepository.findAllByCreatorIdOrderByDateOfPublicationDesc(id);
    }

    //удаляем tag_news
    public void deleteTag_news(Tag_News tag_news){
        tagNewsRepository.delete(tag_news);
    }

    //сообщаем о новости
    public void informAboutNews(ManagementCompany managementCompany,News news){
        List<UserSystem> allUsers=new ArrayList<>();
        for (House house:managementCompany.getHouses()) {
            List<House_User> listHouse_user=house.getHouse_userSet();
            for (House_User house_user:listHouse_user) {
                allUsers.add(house_user.getUserSystem());
            }
        }

        for (UserSystem userSystem:allUsers) {
            if (userSystem.getFlagOnTakeNews()) {
                try {
                    String title="<p style=\"text-align: center;\"><strong>"+news.getHeader()+"</strong></p>";
                    emailMailSender.send(userSystem.getEmail(),managementCompany.getTitle(),title+news.getContent());
                }catch (Exception ignored){}
            }
        }
    }

    public List<News> findAllForTaggedNews(Long tagId,Long mcId){
        List<Tag_News> tagNews=tagNewsRepository.findAllByTagIdAndNews_ManagementCompanyIdOrderByNewsDesc(tagId,mcId);
        List<News> news=new ArrayList<>();
        for (Tag_News tag_news : tagNews){
            news.add(tag_news.getNews());
        }
        return news;
    }

    public List<News> findAllByCreatorIdWithTag(Long tagId,Long creatorId){
        List<Tag_News> tagNews=tagNewsRepository.findAllByTagIdAndNews_CreatorIdOrderByNewsDesc(tagId,creatorId);
        List<News> news=new ArrayList<>();
        for (Tag_News tag_news : tagNews){
            news.add(tag_news.getNews());
        }
        return news;
    }
}
