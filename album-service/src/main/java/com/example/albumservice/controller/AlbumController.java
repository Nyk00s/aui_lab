package com.example.albumservice.controller;

import com.example.albumservice.entity.Album;
import com.example.albumservice.dto.*;
import com.example.albumservice.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public ResponseEntity<List<AlbumListDTO>> listAlbums() {
        List<AlbumListDTO> list = albumService.findAll()
                .stream()
                .map(this::toAlbumListDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumReadDTO> getAlbum(@PathVariable("id") UUID id) {
        return albumService.findById(id)
                .map(this::toAlbumReadDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlbumReadDTO> createAlbum(@RequestBody AlbumCreateUpdateDTO dto) {
        Album album = new Album.Builder()
                .name(dto.getName())
                .author(dto.getAuthor())
                .yearOfRelease(dto.getYearOfRelease())
                .build();
        Album saved = albumService.save(album);
        AlbumReadDTO read = toAlbumReadDTO(saved);
        URI location = URI.create("/api/albums/" + saved.getId());
        return ResponseEntity.created(location).body(read);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumReadDTO> updateAlbum(@PathVariable("id") UUID id, @RequestBody AlbumCreateUpdateDTO dto) {
        return albumService.findById(id).map(existing -> {
            existing = new Album.Builder()
                    .id(existing.getId())
                    .name(dto.getName())
                    .author(dto.getAuthor())
                    .yearOfRelease(dto.getYearOfRelease())
                    .build();
            Album saved = albumService.save(existing);
            return ResponseEntity.ok(toAlbumReadDTO(saved));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable("id") UUID id) {
        return albumService.findById(id).map(a -> {
            albumService.deleteId(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private AlbumListDTO toAlbumListDTO(Album a) {
        return new AlbumListDTO(a.getId().toString(), a.getName(), a.getAuthor());
    }

    private AlbumReadDTO toAlbumReadDTO(Album a) {
        return new AlbumReadDTO(a.getId().toString(), a.getName(), a.getAuthor(), a.getYearOfRelease());
    }
}