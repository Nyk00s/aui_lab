package org.example;

public class SongDTO implements Comparable<SongDTO> {
    private final String name;
    private final int seconds;
    private final String album;

    public SongDTO(String name, int seconds, String album) {
        this.name = name;
        this.seconds = seconds;
        this.album = album;
    }

    public String getName() { return name; }
    public int getSeconds() { return seconds; }
    public String getAlbum() { return album; }

    @Override
    public int compareTo(SongDTO o) {
        if (o == null) return 1;
        int cmp = this.name.compareToIgnoreCase(o.name);
        if (cmp != 0) return cmp;
        return Integer.compare(this.seconds, o.seconds);
    }

    @Override
    public String toString() {
        return "SongDTO{name='" + name + "', seconds=" + seconds + ", album=" + album + "'}";
    }
}
