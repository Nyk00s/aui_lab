package com.example.songservice.dto;

public class SongReadDTO {
    private String id;
    private String name;
    private int seconds;
    private String albumId;
    private String albumName;

    public SongReadDTO() {}

    public SongReadDTO(String id, String name, int seconds, String albumId, String albumName) {
        this.id = id;
        this.name = name;
        this.seconds = seconds;
        this.albumId = albumId;
        this.albumName = albumName;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getSeconds() { return seconds; }
    public String getAlbumId() { return albumId; }
    public String getAlbumName() { return albumName; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSeconds(int seconds) { this.seconds = seconds; }
    public void setAlbumId(String albumId) { this.albumId = albumId; }
    public void setAlbumName(String albumName) { this.albumName = albumName; }
}