package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "songs")
public class Song implements Comparable<Song>, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "seconds", nullable = false)
    private int seconds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    @JsonBackReference
    private Album album;

    public Song() {}

    public Song(String name) {
        this.name = name;
    }

    private Song(Builder b) {
        this.id = b.id != null ? b.id : UUID.randomUUID();
        this.name = b.name;
        this.seconds = b.seconds;
        this.album = b.album;
        if (this.album != null) {
            this.album.addSong(this);
        }
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public int getSeconds() { return seconds; }
    public Album getAlbum() { return album; }

    void setAlbum(Album album) {
        this.album = album;
    }

    public static class Builder {
        private UUID id;
        private String name;
        private int seconds;
        private Album album;

        public Builder id(UUID id){ this.id = id; return this; }
        public Builder name(String name){ this.name = name; return this; }
        public Builder seconds(int seconds){ this.seconds = seconds; return this; }
        public Builder album(Album album){ this.album = album; return this; }
        public Song build(){ return new Song(this); }
    }

    @Override
    public int compareTo(Song o) {
        if (o == null) return 1;
        int cmp = this.name.compareToIgnoreCase(o.name);
        if (cmp != 0) return cmp;
        return Integer.compare(this.seconds, o.seconds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song that = (Song) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Song{id=" + id + ", name='" + name + "', seconds=" + seconds + "}";
    }
}

