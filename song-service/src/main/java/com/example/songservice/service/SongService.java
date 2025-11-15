package com.example.songservice.service;

import com.example.songservice.entity.*;
import com.example.songservice.repository.SongRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SongService {

    private final SongRepository songRepository;
    public SongService(SongRepository songRepository) {this.songRepository = songRepository; }
    public List<Song> findAll() { return songRepository.findAll(); }
    public Optional<Song> findById(UUID id) { return songRepository.findById(id); }
    public List<Song> findByAlbumName(String name) { return songRepository.findByAlbum_NameIgnoreCase(name); }
    public List<Song> findByAlbumId(UUID albumId) {return songRepository.findByAlbum_AlbumId(albumId); }
    public Song save(Song song) { return songRepository.save(song); }
    public void deleteById(UUID id) { songRepository.deleteById(id); }

}

