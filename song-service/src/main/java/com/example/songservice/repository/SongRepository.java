package com.example.songservice.repository;

import java.util.UUID;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.songservice.entity.*;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, UUID> {
    List<Song> findByAlbum_NameIgnoreCase(String albumName);
    List<Song> findByAlbum_AlbumId(UUID album_id);
}