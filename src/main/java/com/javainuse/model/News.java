package com.javainuse.model;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "news")
public class News {
    public News() {
        super();
    }
    public News(String author,Date date,String title, Long imageId,String description) {
        this.title = title;
        this.imageId=imageId;
        this.date=date;
        this.author=author;
        this.description = description;
    }
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "imageId")
    private Long imageId;

    @Column(name = "description",length=5000)
    private String description;

    @Column(name = "date")
    private Date date;

    @Column(name = "author")
    private String author;

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

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
