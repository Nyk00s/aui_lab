package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.SongRepository;
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
    public List<Song> findByAlbum(Album album) { return songRepository.findByAlbum(album); }
    public List<Song> findByAlbumName(String name) { return songRepository.findByAlbum_NameIgnoreCase(name); }
    public Song save(Song song) { return songRepository.save(song); }
    public void deleteById(UUID id) { songRepository.deleteById(id); }

}
