package com.example.demo.dto;

public class SongListDTO {
    private String id;
    private String name;
    private String albumName;

    public SongListDTO() {}

    public SongListDTO(String id, String name, String albumName) {
        this.id = id;
        this.name = name;
        this.albumName = albumName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlbumName() {
        return albumName;
    }
}
