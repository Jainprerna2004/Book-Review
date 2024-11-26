package com.example.bookreview.Model;

public class Book {
    private int id;  // Added to store the ID
    private String title;
    private String author;
    private String year;
    private String platform;
    private int coverImage;

    // Constructor with all parameters
    public Book(int id, String title, String author, String year, String platform, int coverImage) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.platform = platform;
        this.coverImage = coverImage;
    }

    // Default constructor
    public Book() {}

    public Book(String title, String author, String year, String platform, int coverImage) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.platform = platform;
        this.coverImage = coverImage;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    public String getPlatform() {
        return platform;
    }

    public int getCoverImage() {
        return coverImage;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setCoverResourceId(int coverImage) {
        this.coverImage = coverImage;
    }

    public int getCoverResourceId() {
        return coverImage;
    }
}
