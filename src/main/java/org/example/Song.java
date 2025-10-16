package org.example;

import java.io.Serializable;
import java.util.Objects;

public class Song implements Comparable<Song>, Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int seconds;
    private Album album;

    private Song(Builder b) {
        this.name = b.name;
        this.seconds = b.seconds;
        this.album = b.album;
        if (this.album != null) {
            this.album.addSong(this);
        }
    }

    public String getName() { return name; }
    public int getSeconds() { return seconds; }
    public Album getAlbum() { return album; }

    void setAlbum(Album album) {
        this.album = album;
        if (album != null && !album.getSongs().contains(this)) {
            album.addSong(this);
        }
    }

    public static class Builder {
        private String name;
        private int seconds;
        private Album album;


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
        return seconds == that.seconds && Objects.equals(name, that.name) &&
                Objects.equals(album == null ? null : album.getName(),
                that.album == null ? null : that.album.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, seconds, album == null ? null : album.getName());
    }

    @Override
    public String toString() {
        return "Song{name='" + name + "', seconds=" + seconds + ", album='" + (album == null ? "<none>" : album.getName()) + "'}";
    }
}
