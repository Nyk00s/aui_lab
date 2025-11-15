package com.example.songservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class AlbumSimplified {
    @Column(name = "album_id")
    private UUID albumId;

    @Column(name = "album_name")
    private String name;

    public AlbumSimplified() {}

    public AlbumSimplified(UUID id, String name) {
        this.albumId = id;
        this.name = name;
    }

    public UUID getId() { return albumId; }
    public String getName() { return name; }

    public void setId(UUID id) { this.albumId = id; }
    public void setName(String name) { this.name = name; }
}
