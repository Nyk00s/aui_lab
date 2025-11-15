package com.example.songservice.dto;

public class SongListDTO {
    private String id;
    private String name;
    private int seconds;
    private String albumName;

    public SongListDTO() {}

    public SongListDTO(String id, String name, int seconds, String albumName) {
        this.id = id;
        this.name = name;
        this.seconds = seconds;
        this.albumName = albumName;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAlbumName() { return albumName; }
    public int getSeconds() { return seconds; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAlbumName(String albumName) { this.albumName = albumName; }
    public void setSeconds(int seconds) { this.seconds = seconds; }
}