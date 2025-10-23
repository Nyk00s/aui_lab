package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.entity.Album;
import com.example.demo.repository.AlbumRepository;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {this.albumRepository = albumRepository; }
    public List<Album> findAll() { return albumRepository.findAll(); }
    public Optional<Album> findById(UUID id) { return albumRepository.findById(id); }
    public List<Album> findByAuthor(String author) { return albumRepository.findByAuthorIgnoreCase(author); }
    public Album save(Album album) { return albumRepository.save(album); }
    public void deleteId(UUID id) { albumRepository.deleteById(id); }

}
