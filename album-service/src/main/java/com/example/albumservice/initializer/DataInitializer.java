package com.example.albumservice.initializer;


import com.example.albumservice.entity.Album;
import com.example.albumservice.service.AlbumService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final AlbumService albumService;

    public DataInitializer(AlbumService albumService) {
        this.albumService = albumService;
    }

    @PostConstruct
    public void initData() {
        Album hybrid_theory = new Album.Builder().name("Hybrid Theory").author("Linkin Park").yearOfRelease(2000).build();
        Album meteora = new Album.Builder().name("Meteora").author("Linkin Park").yearOfRelease(2003).build();
        Album from_zero = new Album.Builder().name("From Zero").author("Linkin Park").yearOfRelease(2024).build();
        Album nevermind = new Album.Builder().name("Nevermind").author("Nirvana").yearOfRelease(1991).build();
        Album in_utero = new Album.Builder().name("In Utero").author("Nirvana").yearOfRelease(1993).build();
        Album one_x = new Album.Builder().name("One-X").author("Three Days Grace").yearOfRelease(2006).build();

        albumService.save(hybrid_theory);
        albumService.save(meteora);
        albumService.save(from_zero);
        albumService.save(nevermind);
        albumService.save(in_utero);
        albumService.save(one_x);
    }
}
