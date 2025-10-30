package com.example.demo.dto;

import java.util.List;

public class AlbumReadDTO {
    private String id;
    private String name;
    private String author;
    private int yearOfRelease;
    private List<String> songs;

    public AlbumReadDTO() {}

    public AlbumReadDTO(String id, String name, String author, int yearOfRelease, List<String> songs) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.yearOfRelease = yearOfRelease;
        this.songs = songs;
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

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public List<String> getSongs() {
        return songs;
    }
}
