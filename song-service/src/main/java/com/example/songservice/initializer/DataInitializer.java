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

        if (!albumMap.containsKey("Hybrid Theory") || !albumMap.containsKey("Meteora") ||
                !albumMap.containsKey("From Zero") || !albumMap.containsKey("Nevermind") ||
                !albumMap.containsKey("In Utero") || !albumMap.containsKey("One-X")) {
            System.err.println("Not all required albums are available in Album Service.");
            return;
        }

        AlbumDTO hybrid_theory = albumMap.get("Hybrid Theory");
        AlbumDTO meteora = albumMap.get("Meteora");
        AlbumDTO from_zero = albumMap.get("From Zero");
        AlbumDTO nevermind = albumMap.get("Nevermind");
        AlbumDTO in_utero = albumMap.get("In Utero");
        AlbumDTO one_x = albumMap.get("One-X");

        Song in_the_end = new Song.Builder().name("In the End").seconds(216).album(new AlbumSimplified(UUID.fromString(hybrid_theory.getId()), hybrid_theory.getName())).build();
        Song crawling = new Song.Builder().name("Crawling").seconds(208).album(new AlbumSimplified(UUID.fromString(hybrid_theory.getId()), hybrid_theory.getName())).build();
        Song papercut = new Song.Builder().name("Papercut").seconds(184).album(new AlbumSimplified(UUID.fromString(hybrid_theory.getId()), hybrid_theory.getName())).build();
        Song numb = new Song.Builder().name("Numb").seconds(187).album(new AlbumSimplified(UUID.fromString(meteora.getId()), meteora.getName())).build();
        Song faint = new Song.Builder().name("Faint").seconds(216).album(new AlbumSimplified(UUID.fromString(meteora.getId()), meteora.getName())).build();
        Song breaking_the_habit = new Song.Builder().name("Breaking the Habit").seconds(196).album(new AlbumSimplified(UUID.fromString(meteora.getId()), meteora.getName())).build();
        Song the_emptiness_machine = new Song.Builder().name("The Emptiness Machine").seconds(190).album(new AlbumSimplified(UUID.fromString(from_zero.getId()), from_zero.getName())).build();
        Song two_faced = new Song.Builder().name("Two Faced").seconds(183).album(new AlbumSimplified(UUID.fromString(from_zero.getId()), from_zero.getName())).build();
        Song heavy_is_the_crown = new Song.Builder().name("Heavy Is the Crown").seconds(167).album(new AlbumSimplified(UUID.fromString(from_zero.getId()), from_zero.getName())).build();
        Song smells_like_teen_spirit = new Song.Builder().name("Smells Like Teen Spirit").seconds(301).album(new AlbumSimplified(UUID.fromString(nevermind.getId()), nevermind.getName())).build();
        Song come_as_you_are = new Song.Builder().name("Come as you are").seconds(218).album(new AlbumSimplified(UUID.fromString(nevermind.getId()), nevermind.getName())).build();
        Song lithium = new Song.Builder().name("Lithium").seconds(257).album(new AlbumSimplified(UUID.fromString(nevermind.getId()), nevermind.getName())).build();
        Song heart_shaped_box = new Song.Builder().name("Heart-Shaped Box").seconds(281).album(new AlbumSimplified(UUID.fromString(in_utero.getId()), in_utero.getName())).build();
        Song all_apologies = new Song.Builder().name("All Apologies").seconds(233).album(new AlbumSimplified(UUID.fromString(in_utero.getId()), in_utero.getName())).build();
        Song rape_me = new Song.Builder().name("Rape Me").seconds(170).album(new AlbumSimplified(UUID.fromString(in_utero.getId()), in_utero.getName())).build();
        Song animal_i_have_become = new Song.Builder().name("Animal I Have Become").seconds(231).album(new AlbumSimplified(UUID.fromString(one_x.getId()), one_x.getName())).build();
        Song time_of_dying = new Song.Builder().name("Time Of Dying").seconds(186).album(new AlbumSimplified(UUID.fromString(one_x.getId()), one_x.getName())).build();
        Song riot = new Song.Builder().name("Riot").seconds(207).album(new AlbumSimplified(UUID.fromString(one_x.getId()), one_x.getName())).build();

        songService.save(in_the_end);
        songService.save(crawling);
        songService.save(papercut);
        songService.save(numb);
        songService.save(faint);
        songService.save(breaking_the_habit);
        songService.save(the_emptiness_machine);
        songService.save(two_faced);
        songService.save(heavy_is_the_crown);
        songService.save(smells_like_teen_spirit);
        songService.save(come_as_you_are);
        songService.save(lithium);
        songService.save(heart_shaped_box);
        songService.save(all_apologies);
        songService.save(rape_me);
        songService.save(animal_i_have_become);
        songService.save(time_of_dying);
        songService.save(riot);
    }
}
