package com.example.demo.dto;

public class AlbumListDTO {
    private String id;
    private String name;
    private String author;

    public AlbumListDTO() {}

    public AlbumListDTO(String id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
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
}
