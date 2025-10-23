package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Album implements Comparable<Album>, Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final int yearOfRelease;
    private final List<Song> songs = new ArrayList<>();

    private Album(Builder b) {
        this.name = b.name;
        this.yearOfRelease = b.yearOfRelease;
        if (b.songs != null) {
            for (Song s : this.songs) {
                s.setAlbum(this);
            }
        }
    }

    public String getName() { return name; }
    public int getYearOfRelease() { return yearOfRelease; }
    public List<Song> getSongs() { return Collections.unmodifiableList(songs); }

    public void addSong(Song s) {
        if (s == null) return;
        if (!songs.contains(s)) {
            songs.add(s);
        }
        if (s.getAlbum() != this) {
            s.setAlbum(this);
        }
    }


    public static class Builder {
        private String name;
        private int yearOfRelease;
        private List<Song> songs;

        public Builder name(String name){ this.name = name; return this; }
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
        return Objects.equals(name, that.name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Album{name='").append(name).append("', yearOfRelease=").append(yearOfRelease).append(", songs=[");
        for (int i = 0; i < songs.size(); i++) {
            sb.append(songs.get(i).getName());
            if (i < songs.size() - 1) sb.append(',');
        }
        sb.append("]}");
        return sb.toString();

    }
}
