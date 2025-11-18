package com.example.albumservice.controller;

import com.example.albumservice.client.SongClient;
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
    private final SongClient songClient;

    public AlbumController(AlbumService albumService, SongClient songClient) {
        this.albumService = albumService;
        this.songClient = songClient;
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

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<SongDTO>> getAlbumsSongs(@PathVariable("id") UUID id) {
        return albumService.findById(id)
                .map(album -> {
                    List<SongDTO> songs = songClient.getSongsByAlbumId(id);
                    return ResponseEntity.ok(songs);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlbumReadDTO> createAlbum(@RequestBody AlbumCreateUpdateDTO dto) {
        if (albumService.findByName(dto.getName()).isPresent()) {
            return ResponseEntity.status(409).build();
        }
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

            albumService.findByName(dto.getName()).ifPresent(found -> {
                if (!found.getId().equals(id)) {
                    throw new AlbumNameConflictException("Album with name '" + dto.getName() + "' already exists");
                }
            });

            try {
                songClient.updateSongs(id, dto.getName());
            } catch (Exception e){
                System.err.println("Error updating songs for album " + id + ": " + e.getMessage());
            }

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
            try {
                songClient.deleteSongsByAlbumId(id);
            } catch (Exception e) {
                System.err.println("Error deleting songs for album " + id + ": " + e.getMessage());
            }
            albumService.deleteId(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private AlbumListDTO toAlbumListDTO(Album a) {
        return new AlbumListDTO(a.getId().toString(), a.getName(), a.getAuthor(), a.getYearOfRelease());
    }

    private AlbumReadDTO toAlbumReadDTO(Album a) {
        return new AlbumReadDTO(a.getId().toString(), a.getName(), a.getAuthor(), a.getYearOfRelease());
    }

    private static class AlbumNameConflictException extends RuntimeException {
        public AlbumNameConflictException(String message) {
            super(message);
        }
    }
}