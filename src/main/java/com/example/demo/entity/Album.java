package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "albums")
public class Album implements Comparable<Album>, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "year_of_release", nullable = false)
    private int yearOfRelease;

    @JsonBackReference
    @OneToMany(mappedBy = "album", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Song> songs = new ArrayList<>();

    protected Album() {}

    private Album(Builder b) {
        this.id = b.id != null ? b.id : UUID.randomUUID();
        this.name = b.name;
        this.author = b.author;
        this.yearOfRelease = b.yearOfRelease;
        if (b.songs != null) {
            this.songs = b.songs;
            for (Song s : this.songs) {
                s.setAlbum(this);
            }
        }
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getAuthor() { return author; }
    public int getYearOfRelease() { return yearOfRelease; }
    public List<Song> getSongs() { return Collections.unmodifiableList(songs); }

    public void addSong(Song s) {
        if (s == null) return;
        if (!songs.contains(s)) {
            songs.add(s);
            s.setAlbum(this);
        }
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String author;
        private int yearOfRelease;
        private List<Song> songs;

        public Builder id(UUID id){ this.id = id; return this; }
        public Builder name(String name){ this.name = name; return this; }
        public Builder author(String author){ this.author = author; return this; }
        public Builder yearOfRelease(int yearOfRelease){ this.yearOfRelease = yearOfRelease; return this; }
        public Builder songs(List<Song> songs){ this.songs = songs; return this; }
        public Album build(){ return new Album(this); }
    }

    @Override
    public int compareTo(Album o) {
        if (o == null) return 1;
        return this.name.compareToIgnoreCase(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;
        Album that = (Album) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Album{id=" + id + ", name='" + name + "', author='" + author +
                "', yearOfRelease=" + yearOfRelease + "}";
    }
}
