package com.aclib.aclib_deploy.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "aclib_book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Lob
    @Column(name = "thumnail")
    private String thumbnail;

    @Column(name = "id_selfLink", columnDefinition = "VARCHAR(255) NOT NULL")
    private String idSelfLink;

    @Column(name = "SelfLink", nullable = false)
    private String selfLink;

    @Column(name = "publishdate")
    private String publishDate;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "added_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedDate;

    @Column
    private String category;

    @Column
    private String description;

    @Column
    private int pageCount;

    @Column(name = "copy_count", nullable = false)
    private int copy = 0;

    /**
     * always set date when saving new book and printing with tag recently add maybe.
     */
    @PrePersist
    protected void onCreate() {
        addedDate = new Date();
    }

    //getter and setter

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdSelfLink() {
        return idSelfLink;
    }

    public void setIdSelfLink(String idSelfLink) {
        this.idSelfLink = idSelfLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getCopy() {
        return copy;
    }

    public void setCopy(int copy) {
        this.copy = copy;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getCategory() {return category; }

    public void setCategory(String category) {this.category = category; }
}

