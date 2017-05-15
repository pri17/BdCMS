package com.bidanet.bdcms.entity;

import javax.persistence.*;

/**
 * Created by gao on 2017/2/27.
 */
@Entity
@Table(name = "exam_index_image")
public class ExamIndexImage {
    private Long id;

    private String title;

    private String url;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
