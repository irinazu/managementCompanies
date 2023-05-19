package com.micro.managementCompanies.modelsForSend;

import com.micro.managementCompanies.models.News;
import com.micro.managementCompanies.models.Tag;
import com.micro.managementCompanies.models.Tag_News;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@Getter
@Setter
public class NewsDTO {
    private Long id;
    private String header;
    private String content;
    private Date dateOfPublication;
    private List<Tag> tagList=new ArrayList<>();
    UserSystemDTO creator=new UserSystemDTO();
    ManagementCompanyDTO managementCompanyDTO=new ManagementCompanyDTO();

    public void setArgs(News news) {
        this.id = news.getId();
        this.header = news.getHeader();
        this.content = news.getContent();
        this.dateOfPublication = news.getDateOfPublication();
        if(!news.getTag_newsSet().isEmpty()){
            setTags(news.getTag_newsSet());
        }
        creator.setAllArgs(news.getCreator(),-1);
        managementCompanyDTO.setArgs(news.getManagementCompany());
    }
    public void setTags(List<Tag_News> tag_news){
        for (Tag_News tagNews:tag_news) {
            tagList.add(tagNews.getTag());
        }
    }

    @Override
    public String toString() {
        return "NewsDTO{" +
                "id=" + id +
                ", header='" + header + '\'' +
                ", content='" + content + '\'' +
                ", dateOfPublication=" + dateOfPublication +
                ", tagList=" + tagList +
                '}';
    }
}
