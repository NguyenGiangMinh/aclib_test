package com.aclib.aclib_deploy.ThirdPartyService;

public class VolumeInfo {
    private String title;
    private String[] authors;
    private String description;
    private ImageLinks imageLinks;
    private String publisher;
    private String publishedDate;
    private String[] categories;
    private int pageCount;
    private String language;
    private boolean availble;


    // Getters and setters
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


    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean isAvailble() {
        return availble;
    }

    public void setAvailble(boolean availble) {
        this.availble = availble;
    }

    public String[] getCategories() {return categories; }

    public void setCategories(String[] categories) {this.categories = categories; }
}
