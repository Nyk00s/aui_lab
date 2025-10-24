package com.example.demo.cli;

import com.example.demo.entity.Album;
import com.example.demo.entity.Song;
import com.example.demo.service.AlbumService;
import com.example.demo.service.SongService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final AlbumService albumService;
    @Autowired
    private final SongService songService;

    public ConsoleRunner(AlbumService albumService, SongService songService) {
        this.albumService = albumService;
        this.songService = songService;
    }

    @Transactional
    @Override
    public void run(String... args) {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n> ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "help" -> printHelp();
                case "list albums" -> listAlbums();
                case "list songs" -> listSongs();
                case "find album" -> findAlbum(scanner);
                case "find song" -> findSong(scanner);
                case "search albums" -> searchAlbums(scanner);
                case "search songs" -> searchSongs(scanner);
                case "add album" -> addAlbum(scanner);
                case "add song" -> addSong(scanner);
                case "delete album" -> deleteAlbum(scanner);
                case "delete song" -> deleteSong(scanner);
                case "exit" -> {
                    running = false;
                    System.out.println("Exit application");
                }
                default -> System.out.println("Unknown command. Type 'help' for available command.");
            }
        }
        scanner.close();
    }

    private void printHelp() {
        System.out.println("""
                Available commands:
                  help             - Show this help message
                  list albums      - List all albums
                  list songs       - List all songs
                  find album       - Find album by ID
                  find song        - Find song by ID
                  search albums    - Search albums by author
                  search songs     - Search songs by album name
                  add album        - Add a new album
                  add song         - Add a new song (choose album)
                  delete album     - Delete an album by ID
                  delete song      - Delete a song by ID
                  exit             - Stop the application
                """);
    }

    private void listAlbums() {
        List<Album> albums = albumService.findAll();
        if (albums.isEmpty()) {
            System.out.println("No albums found.");
        }
        else {
            System.out.println("Albums: ");
            albums.forEach(a -> {
                System.out.printf("  [%s] %s (%d) by %s%n",
                        a.getId(), a.getName(), a.getYearOfRelease(), a.getAuthor());
            });
        }
    }

    private void listSongs() {
        List<Song> songs = songService.findAll();
        if (songs.isEmpty()) {
            System.out.println("No songs found.");
        } else {
            System.out.println("Songs: ");
            songs.forEach(s -> {
                System.out.printf("  [%s] %s (%d s) from '%s'%n",
                        s.getId(), s.getName(), s.getSeconds(),
                        s.getAlbum() != null ? s.getAlbum().getName() : "no album");
            });
        }
    }

    private void findAlbum(Scanner scanner) {
        listAlbums();
        System.out.println("Enter album ID: ");
        String id = scanner.nextLine();
        try {
            UUID uuid = UUID.fromString(id);
            Optional<Album> album = albumService.findById(uuid);
            album.ifPresentOrElse(
                    a -> System.out.printf("Album: %s (%d) by %s%n", a.getName(), a.getYearOfRelease(), a.getAuthor()),
                    () -> System.out.println("Album not found.")
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID");
        }
    }

    private void findSong(Scanner scanner) {
        listSongs();
        System.out.println("Enter song ID: ");
        String id = scanner.nextLine();
        try {
            UUID uuid = UUID.fromString(id);
            Optional<Song> song = songService.findById(uuid);
            song.ifPresentOrElse(
                    s -> System.out.printf("Song: %s (%d) from %s%n", s.getName(), s.getSeconds(), s.getAlbum().getName()),
                    () -> System.out.println("Song not found.")
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID: " + e.getMessage());
        }
    }


    private void searchAlbums(Scanner scanner) {
        System.out.println("Search by 'author': ");
        String mode = scanner.nextLine().trim().toLowerCase();
        if (!mode.equals("author")) {
            System.out.println("Unknown search mode. Use 'author'.");
            return;
        }

        System.out.println("Enter search term: ");
        String term = scanner.nextLine();

        List<Album> results = albumService.findByAuthor(term);

        if (results.isEmpty()) {
            System.out.println("No matching albums");
        } else {
            System.out.println("Found albums: ");
            results.forEach(a -> {
                System.out.printf("  [%s] %s (%d) by %s%n",
                        a.getId(), a.getName(), a.getYearOfRelease(), a.getAuthor());
            });
        }
    }

    private void searchSongs(Scanner scanner) {
        System.out.println("Search by 'album name': ");
        String mode = scanner.nextLine().trim().toLowerCase();
        if (!mode.equals("album name")) {
            System.out.println("Unknown search mode. Use 'album name'.");
            return;
        }

        System.out.println("Enter search term: ");
        String term = scanner.nextLine();

        List<Song> results = songService.findByAlbumName(term);

        if (results.isEmpty()) {
            System.out.println("No matching album name");
        } else {
            System.out.printf("Found album %s%nSongs:%n", results.getFirst().getAlbum().getName());
            results.forEach(s -> {
                System.out.printf("  [%s] %s (%d)%n", s.getId(), s.getName(), s.getSeconds());
            });
        }
    }



    private void addAlbum(Scanner scanner) {
        System.out.println("Enter album name: ");
        String name = scanner.nextLine();

        System.out.println("Enter album author: ");
        String author = scanner.nextLine();

        System.out.println("Enter year of release: ");
        int year = Integer.parseInt(scanner.nextLine());

        Album album = new Album.Builder()
                .name(name)
                .author(author)
                .yearOfRelease(year)
                .build();

        albumService.save(album);
        System.out.println("Album added.");
    }

    private void addSong(Scanner scanner) {
        List<Album> albums = albumService.findAll();
        if (albums.isEmpty()) {
            System.out.println("No albums available. Please add an album first.");
            return;
        }

        System.out.println("Enter song name: ");
        String name = scanner.nextLine();

        System.out.println("Enter song duration (seconds): ");
        int seconds = Integer.parseInt(scanner.nextLine());

        System.out.println("Choose album (number): ");
        for (int i = 0; i < albums.size(); i++) {
            System.out.printf("  %d: %s (%s)%n", i, albums.get(i).getName(), albums.get(i).getAuthor());
        }

        int choice = Integer.parseInt(scanner.nextLine());
        if (choice < 0 || choice >= albums.size()) {
            System.out.println("Invalid choice");
            return;
        }

        Album chosen = albums.get(choice);
        Song song = new Song.Builder()
                .name(name)
                .seconds(seconds)
                .album(chosen)
                .build();

        songService.save(song);
        System.out.println("Song added.");
    }

    private void deleteAlbum(Scanner scanner) {
        listAlbums();
        System.out.println("Enter album ID to delete: ");
        String id = scanner.nextLine();
        try {
            UUID uuid = UUID.fromString(id);
            albumService.deleteId(uuid);
            System.out.println("Album deleted.");
        } catch (Exception e) {
            System.out.println("Could not delete album: " + e.getMessage());
        }
    }

    private void deleteSong(Scanner scanner) {
        listSongs();
        System.out.println("Enter song ID to delete: ");
        String id = scanner.nextLine();
        try {
            UUID uuid = UUID.fromString(id);
            songService.deleteById(uuid);
            System.out.println("Song deleted.");
        } catch (Exception e) {
            System.out.println("Could not delete song: " + e.getMessage());
        }
    }

}
