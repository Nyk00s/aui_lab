package com.example.demo.controller;


import com.example.demo.dto.SongCreateUpdateDTO;
import com.example.demo.dto.SongListDTO;
import com.example.demo.dto.SongReadDTO;
import com.example.demo.entity.Song;
import com.example.demo.service.AlbumService;
import com.example.demo.service.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SongController {

    private final SongService songService;
    private final AlbumService albumService;

    public SongController(SongService songService, AlbumService albumService) {
        this.songService = songService;
        this.albumService = albumService;
    }

    @GetMapping("/songs")
    public ResponseEntity<List<SongListDTO>> listSongs() {
        List<SongListDTO> list = songService.findAll()
                .stream()
                .map(this::toSongListDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<SongReadDTO> getSong(@PathVariable("id") UUID id) {
        return songService.findById(id)
                .map(this::toSongReadDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/albums/{albumId}/songs")
    public ResponseEntity<SongReadDTO> createSong(@PathVariable("albumId") UUID albumId, @RequestBody SongCreateUpdateDTO dto) {
        return albumService.findById(albumId).map(a -> {
            Song s = new Song.Builder()
                    .name(dto.getName())
                    .seconds(dto.getSeconds())
                    .album(a)
                    .build();
            Song saved = songService.save(s);
            URI location = URI.create("/api/songs/" + saved.getId());
            return ResponseEntity.created(location).body(toSongReadDTO(saved));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/songs/{id}")
    public ResponseEntity<SongReadDTO> updateSong(@PathVariable("id") UUID id, @RequestBody SongCreateUpdateDTO dto) {
        return songService.findById(id).map(existing -> {
            existing = new Song.Builder()
                    .id(existing.getId())
                    .name(existing.getName())
                    .seconds(existing.getSeconds())
                    .album(existing.getAlbum())
                    .build();
            Song saved = songService.save(existing);
            return ResponseEntity.ok(toSongReadDTO(saved));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/songs/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable("id") UUID id) {
        return songService.findById(id).map(s -> {
            songService.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private SongListDTO toSongListDTO(Song s) {
        String albumName = s.getAlbum() != null ? s.getAlbum().getName() : null;
        return new SongListDTO(s.getId().toString(), s.getName(), albumName);
    }

    private SongReadDTO toSongReadDTO(Song s) {
        String albumName = s.getAlbum() != null ? s.getAlbum().getName() : null;
        String albumAuthor = s.getAlbum() != null ? s.getAlbum().getAuthor() : null;
        return new SongReadDTO(s.getId().toString(), s.getName(), s.getSeconds(), albumName, albumAuthor);
    }

}
