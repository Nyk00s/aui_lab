package com.example.songservice.entity;

import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class AlbumSimplified {
    private UUID id;
    private String name;

    public AlbumSimplified() {}

    public AlbumSimplified(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }

    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}
