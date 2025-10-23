package com.example.demo.repository;

import java.util.UUID;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.*;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, UUID> {
    List<Song> findByAlbum(Album album);
    List<Song> findByAlbum_NameIgnoreCase(String albumName);
}
