package com.example.demo.dto;

public class SongCreateUpdateDTO {
    private String name;
    private int seconds;
    private String albumId;

    public SongCreateUpdateDTO() {}

    public SongCreateUpdateDTO(String name, int seconds, String albumId) {
        this.name = name;
        this.seconds = seconds;
        this.albumId = albumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
