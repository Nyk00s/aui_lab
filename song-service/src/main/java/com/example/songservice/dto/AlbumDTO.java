package com.example.songservice.dto;

public class AlbumDTO {
    private String id;
    private String name;
    private String author;
    private int yearOfRelease;

    public AlbumDTO() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAuthor() { return author; }
    public int getYearOfRelease() { return yearOfRelease; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAuthor(String author) { this.author = author; }
    public void setYearOfRelease(int yearOfRelease) { this.yearOfRelease = yearOfRelease; }
}
