package com.micro.managementCompanies.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class TagNewsKey implements Serializable {
    @Column(name = "tag_id")
    Long tagId;

    @Column(name = "news_id")
    Long newsId;
}
