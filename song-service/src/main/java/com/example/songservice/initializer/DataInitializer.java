package com.example.songservice.initializer;


import com.example.songservice.client.AlbumClient;
import com.example.songservice.dto.AlbumDTO;
import com.example.songservice.entity.AlbumSimplified;
import com.example.songservice.entity.Song;
import com.example.songservice.service.SongService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DataInitializer {
    private final SongService songService;
    private final AlbumClient albumClient;

    public DataInitializer(SongService songService, AlbumClient albumClient) {
        this.songService = songService;
        this.albumClient = albumClient;
    }

    @PostConstruct
    public void initData() {
        System.out.println("Waiting for Album Service to initialize...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<AlbumDTO> albums;
        try {
            albums = albumClient.getAllAlbums();
        } catch (Exception e) {
            System.err.println("Could not fetch albums from Album Service: " + e.getMessage());
            System.err.println("Make sure Album Service is running on the configured URL.");
            return;
        }

        Map<String, AlbumDTO> albumMap = albums.stream()
                .collect(Collectors.toMap(AlbumDTO::getName, album -> album));

        String[] requiredAlbums = {
            "Hybrid Theory", "Meteora", "From Zero", "Nevermind", "In Utero", "One-X"
        };

        for (String albumName : requiredAlbums) {
            if (!albumMap.containsKey(albumName)) {
                System.err.println("Required album missing in Album Service: " + albumName);
                return;
            }
        }

        AlbumDTO hybrid_theory = albumMap.get("Hybrid Theory");
        AlbumDTO meteora = albumMap.get("Meteora");
        AlbumDTO from_zero = albumMap.get("From Zero");
        AlbumDTO nevermind = albumMap.get("Nevermind");
        AlbumDTO in_utero = albumMap.get("In Utero");
        AlbumDTO one_x = albumMap.get("One-X");

        Song[] songs = new Song[] {
            new Song.Builder().name("In the End").seconds(216).album(new AlbumSimplified(UUID.fromString(hybrid_theory.getId()), hybrid_theory.getName())).build(),
            new Song.Builder().name("Crawling").seconds(208).album(new AlbumSimplified(UUID.fromString(hybrid_theory.getId()), hybrid_theory.getName())).build(),
            new Song.Builder().name("Papercut").seconds(184).album(new AlbumSimplified(UUID.fromString(hybrid_theory.getId()), hybrid_theory.getName())).build(),
            new Song.Builder().name("Numb").seconds(187).album(new AlbumSimplified(UUID.fromString(meteora.getId()), meteora.getName())).build(),
            new Song.Builder().name("Faint").seconds(216).album(new AlbumSimplified(UUID.fromString(meteora.getId()), meteora.getName())).build(),
            new Song.Builder().name("Breaking the Habit").seconds(196).album(new AlbumSimplified(UUID.fromString(meteora.getId()), meteora.getName())).build(),
            new Song.Builder().name("The Emptiness Machine").seconds(190).album(new AlbumSimplified(UUID.fromString(from_zero.getId()), from_zero.getName())).build(),
            new Song.Builder().name("Two Faced").seconds(183).album(new AlbumSimplified(UUID.fromString(from_zero.getId()), from_zero.getName())).build(),
            new Song.Builder().name("Heavy Is the Crown").seconds(167).album(new AlbumSimplified(UUID.fromString(from_zero.getId()), from_zero.getName())).build(),
            new Song.Builder().name("Smells Like Teen Spirit").seconds(301).album(new AlbumSimplified(UUID.fromString(nevermind.getId()), nevermind.getName())).build(),
            new Song.Builder().name("Come as You Are").seconds(218).album(new AlbumSimplified(UUID.fromString(nevermind.getId()), nevermind.getName())).build(),
            new Song.Builder().name("Lithium").seconds(257).album(new AlbumSimplified(UUID.fromString(nevermind.getId()), nevermind.getName())).build(),
            new Song.Builder().name("Heart-Shaped Box").seconds(281).album(new AlbumSimplified(UUID.fromString(in_utero.getId()), in_utero.getName())).build(),
            new Song.Builder().name("All Apologies").seconds(233).album(new AlbumSimplified(UUID.fromString(in_utero.getId()), in_utero.getName())).build(),
            new Song.Builder().name("Rape Me").seconds(170).album(new AlbumSimplified(UUID.fromString(in_utero.getId()), in_utero.getName())).build(),
            new Song.Builder().name("Animal I Have Become").seconds(231).album(new AlbumSimplified(UUID.fromString(one_x.getId()), one_x.getName())).build(),
            new Song.Builder().name("Time Of Dying").seconds(186).album(new AlbumSimplified(UUID.fromString(one_x.getId()), one_x.getName())).build(),
            new Song.Builder().name("Riot").seconds(207).album(new AlbumSimplified(UUID.fromString(one_x.getId()), one_x.getName())).build()
        };

        for (Song song : songs) {
            if (!songService.existsByNameAndAlbumName(song.getName(), song.getAlbum().getName())) {
                songService.save(song);
            }
        }
    }
}
