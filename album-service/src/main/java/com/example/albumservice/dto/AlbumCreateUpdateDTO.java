package com.example.albumservice.dto;

public class AlbumCreateUpdateDTO {
    private String name;
    private String author;
    private int yearOfRelease;

    public AlbumCreateUpdateDTO() {}

    public AlbumCreateUpdateDTO(String name, String author, int yearOfRelease) {
        this.name = name;
        this.author = author;
        this.yearOfRelease = yearOfRelease;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }
}