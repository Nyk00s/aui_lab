package com.example.demo.dto;

public class SongReadDTO {
    private String id;
    private String name;
    private int seconds;
    private String albumName;
    private String albumAuthor;

    public SongReadDTO() {}

    public SongReadDTO(String id, String name, int seconds, String albumName, String albumAuthor) {
        this.id = id;
        this.name = name;
        this.seconds = seconds;
        this.albumName = albumName;
        this.albumAuthor = albumAuthor;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSeconds() {
        return seconds;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumAuthor() {
        return albumAuthor;
    }
}
