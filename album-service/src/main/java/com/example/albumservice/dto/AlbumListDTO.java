package com.example.albumservice.dto;

public class AlbumListDTO {
    private String id;
    private String name;
    private String author;
    private int yearOfRelease;

    public AlbumListDTO() {}

    public AlbumListDTO(String id, String name, String author, int yearOfRelease) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.yearOfRelease = yearOfRelease;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public int getYearOfRelease() { return yearOfRelease; }
}
