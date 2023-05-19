package com.micro.managementCompanies.models;

import com.micro.managementCompanies.modelsForSend.NewsDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String header;
    private String content;
    private String picture;
    private Date dateOfPublication;

    public News() {}

    public News(NewsDTO news){
        this.header = news.getHeader();
        this.content = news.getContent();
        this.dateOfPublication = new Date();
    }

    public void setArgs(NewsDTO news){
        this.header = news.getHeader();
        this.content = news.getContent();
    }

    @OneToMany(mappedBy = "news")
    List<Tag_News> tag_newsSet;

    @ManyToOne
    ManagementCompany managementCompany;

    @ManyToMany
    List<House> list;

    @ManyToOne
    UserSystem creator;
}
