package com.example.songservice.controller;

import com.example.songservice.dto.AlbumDTO;
import com.example.songservice.dto.SongCreateUpdateDTO;
import com.example.songservice.dto.SongListDTO;
import com.example.songservice.dto.SongReadDTO;
import com.example.songservice.entity.Song;
import com.example.songservice.entity.AlbumSimplified;
import com.example.songservice.client.AlbumClient;
import com.example.songservice.service.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongService songService;
    private final AlbumClient albumClient;

    public SongController(SongService songService, AlbumClient albumClient) {
        this.albumClient = albumClient;
        this.songService = songService;
    }

    @GetMapping
    public ResponseEntity<List<SongListDTO>> getAllSongs() {
        List<SongListDTO> songs = songService.findAll()
                .stream()
                .map(this::toSongListDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongReadDTO> getSongById(@PathVariable UUID id) {
        return songService.findById(id)
                .map(this::toSongReadDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SongReadDTO> createSong(@RequestBody SongCreateUpdateDTO dto) {
        AlbumDTO album = albumClient.getAlbumById(dto.getAlbumId());
        if (album == null) {
            return ResponseEntity.badRequest().build();
        }
        if (dto.getSeconds() < 0) {
            return ResponseEntity.badRequest().build();
        }
        if (Objects.equals(dto.getName(), album.getName())) {
            return ResponseEntity.status(409).build();
        }

        Song song = new Song.Builder()
                .name(dto.getName())
                .seconds(dto.getSeconds())
                .album(new AlbumSimplified(dto.getAlbumId(), album.getName()))
                .build();

        Song saved = songService.save(song);
        URI location = URI.create("/api/songs/" + saved.getId());
        return ResponseEntity.created(location).body(toSongReadDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SongReadDTO> updateSong(@PathVariable UUID id, @RequestBody SongCreateUpdateDTO dto) {
        return songService.findById(id)
                .map(existing -> {
                    if (dto.getAlbumId() != null) {
                        AlbumDTO album = albumClient.getAlbumById(dto.getAlbumId());
                        if (album == null) {
                            return ResponseEntity.badRequest().<SongReadDTO>build();
                        }
                        if (Objects.equals(dto.getName(), album.getName())) {
                            return ResponseEntity.status(409).<SongReadDTO>build();
                        }
                        existing.setAlbum(new AlbumSimplified(dto.getAlbumId(), album.getName()));
                    }

                    existing.setName(dto.getName());
                    existing.setSeconds(dto.getSeconds());

                    Song updated = songService.save(existing);
                    return ResponseEntity.ok(toSongReadDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable UUID id) {
        return songService.findById(id)
                .map(song -> {
                    songService.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<SongReadDTO>> getSongsByAlbumId(@PathVariable UUID albumId) {
        List<SongReadDTO> songs = songService.findByAlbumId(albumId)
                .stream()
                .map(this::toSongReadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(songs);
    }

    @DeleteMapping("/album/{albumId}")
    public ResponseEntity<Void> deleteSongsByAlbumId(@PathVariable UUID albumId) {
        List<Song> songs = songService.findByAlbumId(albumId);

        if (songs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        for (Song song : songs) {
            songService.deleteById(song.getId());
        }

        System.out.println("Deleted " + songs.size() + " songs for album: " + albumId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/album/{albumId}")
    public ResponseEntity<List<SongListDTO>> updateSongs(@PathVariable UUID albumId, @RequestBody String albumName) {
        List<Song> songs = songService.findByAlbumId(albumId);

        if (songs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<SongListDTO> songsDTO = new ArrayList<>();
        for (Song song : songs) {
            song.setAlbum(new AlbumSimplified(albumId, albumName));
            Song updated = songService.save(song);
            songsDTO.addLast(toSongListDTO(updated));
        }
        return ResponseEntity.ok(songsDTO);
    }

    private SongListDTO toSongListDTO(Song s) {
        String albumName = s.getAlbum() != null ? s.getAlbum().getName() : null;
        return new SongListDTO(s.getId().toString(), s.getName(), s.getSeconds(), albumName);
    }

    private SongReadDTO toSongReadDTO(Song s) {
        String albumId = s.getAlbum() != null ? s.getAlbum().getId().toString() : null;
        String albumName = s.getAlbum() != null ? s.getAlbum().getName() : null;
        return new SongReadDTO(s.getId().toString(), s.getName(), s.getSeconds(), albumId, albumName);
    }
}
