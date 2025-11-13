package com.example.demo.initializer;


import com.example.demo.entity.Album;
import com.example.demo.entity.Song;
import com.example.demo.service.AlbumService;
import com.example.demo.service.SongService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final AlbumService albumService;
    private final SongService songService;

    public DataInitializer(AlbumService albumService, SongService songService) {
        this.albumService = albumService;
        this.songService = songService;
    }

    @PostConstruct
    public void initData() {
        Album hybrid_theory = new Album.Builder().name("Hybrid Theory").author("Linkin Park").yearOfRelease(2000).build();
        Album meteora = new Album.Builder().name("Meteora").author("Linkin Park").yearOfRelease(2003).build();
        Album from_zero = new Album.Builder().name("From Zero").author("Linkin Park").yearOfRelease(2024).build();
        Album nevermind = new Album.Builder().name("Nevermind").author("Nirvana").yearOfRelease(1991).build();
        Album in_utero = new Album.Builder().name("In Utero").author("Nirvana").yearOfRelease(1993).build();
        Album one_x = new Album.Builder().name("One-X").author("Three Days Grace").yearOfRelease(2006).build();

        hybrid_theory = albumService.save(hybrid_theory);
        meteora = albumService.save(meteora);
        from_zero = albumService.save(from_zero);
        nevermind = albumService.save(nevermind);
        in_utero = albumService.save(in_utero);
        one_x = albumService.save(one_x);

        Song in_the_end = new Song.Builder().name("In the End").seconds(216).album(hybrid_theory).build();
        Song crawling = new Song.Builder().name("Crawling").seconds(208).album(hybrid_theory).build();
        Song papercut = new Song.Builder().name("Papercut").seconds(184).album(hybrid_theory).build();
        Song numb = new Song.Builder().name("Numb").seconds(187).album(meteora).build();
        Song faint = new Song.Builder().name("Faint").seconds(216).album(meteora).build();
        Song breaking_the_habit = new Song.Builder().name("Breaking the Habit").seconds(196).album(meteora).build();
        Song the_emptiness_machine = new Song.Builder().name("The Emptiness Machine").seconds(190).album(from_zero).build();
        Song two_faced = new Song.Builder().name("Two Faced").seconds(183).album(from_zero).build();
        Song heavy_is_the_crown = new Song.Builder().name("Heavy Is the Crown").seconds(167).album(from_zero).build();
        Song smells_like_teen_spirit = new Song.Builder().name("Smells Like Teen Spirit").seconds(301).album(nevermind).build();
        Song come_as_you_are = new Song.Builder().name("Come as you are").seconds(218).album(nevermind).build();
        Song lithium = new Song.Builder().name("Lithium").seconds(257).album(nevermind).build();
        Song heart_shaped_box = new Song.Builder().name("Heart-Shaped Box").seconds(281).album(in_utero).build();
        Song all_apologies = new Song.Builder().name("All Apologies").seconds(233).album(in_utero).build();
        Song rape_me = new Song.Builder().name("Rape Me").seconds(170).album(in_utero).build();
        Song animal_i_have_become = new Song.Builder().name("Animal I Have Become").seconds(231).album(one_x).build();
        Song time_of_dying = new Song.Builder().name("Time Of Dying").seconds(186).album(one_x).build();
        Song riot = new Song.Builder().name("Riot").seconds(207).album(one_x).build();

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
