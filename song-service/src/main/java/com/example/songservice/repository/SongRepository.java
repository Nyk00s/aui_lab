package com.example.songservice.repository;

import java.util.UUID;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.songservice.entity.*;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, UUID> {
    List<Song> findByAlbum_NameIgnoreCase(String albumName);
    List<Song> findByAlbum_AlbumId(UUID album_id);
    Optional<Song> findByNameAndAlbumName(String name, String albumName);
}