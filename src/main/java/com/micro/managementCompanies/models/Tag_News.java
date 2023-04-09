package com.micro.managementCompanies.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Tag_News {
    @EmbeddedId
    private TagNewsKey tagNewsKey;

    @ManyToOne
    @MapsId("tagId")
    Tag tag;

    @ManyToOne
    @MapsId("newsId")
    News news;
}
