package com.example.songservice.dto;

import java.util.UUID;

public class SongCreateUpdateDTO {
    private String name;
    private int seconds;
    private UUID albumId;

    public SongCreateUpdateDTO() {}

    public SongCreateUpdateDTO(String name, int seconds, UUID albumId) {
        this.name = name;
        this.seconds = seconds;
        this.albumId = albumId;
    }

    public String getName() { return name; }
    public int getSeconds() { return seconds; }
    public UUID getAlbumId() { return albumId; }

    public void setName(String name) { this.name = name; }
    public void setSeconds(int seconds) { this.seconds = seconds; }
    public void setAlbumId(UUID albumId) { this.albumId = albumId; }
}