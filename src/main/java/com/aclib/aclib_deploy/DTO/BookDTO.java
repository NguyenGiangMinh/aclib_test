package com.aclib.aclib_deploy.DTO;

public class BookDTO {
    private String title;
    private String[] authors;
    private String id; //id cua selfLink
    private String selfLink;
    private String thumbnail;
    private String description;
    private String[] category;
    private String publisher;
    private String publishedDate;
    private int pageCount;
    private String language;
    private boolean availableForBorrowing;
    private String status; // available or not; fetch with book entity
    private int copy; //fetch with book entity

    public BookDTO(String title, String[] authors,
                   String id, String selfLink,
                   String thumbnail ,String description, String[] category,
                   String publisher, String publishedDate,
                   int pageCount, String language, boolean availableForBorrowing) {
        this.title = title;
        this.authors = authors;
        this.id = id;
        this.selfLink = selfLink;
        this.thumbnail = thumbnail;
        this.description = description;
        this.category = category;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
        this.language = language;
        this.availableForBorrowing = availableForBorrowing;
    }

    public BookDTO(String title, String[] authors, String idSelfLink,
                   String selfLink, String thumbnail, String publisher,
                   String publishDate, String status,int copy) {
        this.title = title;
        this.authors = authors;
        this.id = idSelfLink;
        this.selfLink = selfLink;
        this.thumbnail = thumbnail;
        this.publisher = publisher;
        this.publishedDate = publishDate;
        this.status = status;
        this.copy = copy;
    }


    public BookDTO(String title, String[] authors, String idSelfLink,
                   String selfLink, String thumbnail, String publisher, int pageCount,
                   String description, String publishDate, String status,int copy) {
        this.title = title;
        this.authors = authors;
        this.id = idSelfLink;
        this.selfLink = selfLink;
        this.thumbnail = thumbnail;
        this.publisher = publisher;
        this.pageCount = pageCount;
        this.description = description;
        this.publishedDate = publishDate;
        this.status = status;
        this.copy = copy;
    }


    //Getter and Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailableForBorrowing() {
        return availableForBorrowing;
    }

    public void setAvailableForBorrowing(boolean availableForBorrowing) {
        this.availableForBorrowing = availableForBorrowing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getCategory() {return category; }

    public void setCategory(String[] category) {this.category = category; }
}
