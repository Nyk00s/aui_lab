package com.example.demo;

import com.example.demo.dto.SongCreateUpdateDTO;
import com.example.demo.entity.Album;
import com.example.demo.entity.Song;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private static UUID albumId;
    private static UUID songId;

    private HttpHeaders headers() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        return h;
    }

    @Test
    @Order(1)
    void shouldCreateAlbum() {
        Album album = new Album.Builder()
                .name("Test Album")
                .author("Test Author")
                .yearOfRelease(2025)
                .build();

        HttpEntity<Album> request = new HttpEntity<>(album, headers());
        ResponseEntity<Album> response = restTemplate.postForEntity(baseUrl() + "/api/albums", request, Album.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        albumId = response.getBody().getId();
        assertEquals("Test Album", response.getBody().getName());
    }

    @Test
    @Order(2)
    void shouldGetAlbumById() {
        ResponseEntity<Album> response = restTemplate.getForEntity(baseUrl() + "/api/albums/" + albumId, Album.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(albumId, response.getBody().getId());
    }

    @Test
    @Order(3)
    void shouldGetAllAlbums() {
        ResponseEntity<Album[]> response = restTemplate.getForEntity(baseUrl() + "/api/albums", Album[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    @Order(4)
    void shouldUpdateAlbum() {
        Album updated = new Album.Builder()
                .name("Updated Album")
                .author("Test Author")
                .yearOfRelease(2026)
                .build();

        HttpEntity<Album> request = new HttpEntity<>(updated, headers());
        restTemplate.put(baseUrl() + "/api/albums/" + albumId, request);

        ResponseEntity<Album> response = restTemplate.getForEntity(baseUrl() + "/api/albums/" + albumId, Album.class);
        assertNotNull(response.getBody());
        assertEquals("Updated Album", response.getBody().getName());
        assertEquals(2026, response.getBody().getYearOfRelease());
    }

    @Test
    @Order(5)
    void shouldDeleteAlbum() {
        restTemplate.delete(baseUrl() + "/api/albums/" + albumId);

        ResponseEntity<Album> response = restTemplate.getForEntity(baseUrl() + "/api/albums/" + albumId, Album.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(6)
    void shouldCreateSong() {
        Album album = new Album.Builder()
                .name("Album for Song")
                .author("Author")
                .yearOfRelease(2025)
                .build();

        HttpEntity<Album> albumRequest = new HttpEntity<>(album, headers());
        albumId = Objects.requireNonNull(restTemplate.postForEntity(baseUrl() + "/api/albums", albumRequest, Album.class).getBody()).getId();

        Song song = new Song.Builder()
                .name("Test Song")
                .seconds(180)
                .build();

        HttpEntity<Song> songRequest = new HttpEntity<>(song, headers());
        ResponseEntity<Song> response = restTemplate.postForEntity("/api/albums/" + albumId + "/songs", songRequest, Song.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        songId = response.getBody().getId();
        assertEquals("Test Song", response.getBody().getName());
    }

    @Test
    @Order(7)
    void shouldGetSongById() {
        ResponseEntity<Song> response = restTemplate.getForEntity(baseUrl() + "/api/songs/" + songId, Song.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Song", response.getBody().getName());
    }

    @Test
    @Order(8)
    void shouldGetAllSongs() {
        ResponseEntity<Song[]> response = restTemplate.getForEntity(baseUrl() + "/api/songs", Song[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    @Order(9)
    void shouldUpdateSong() {
        SongCreateUpdateDTO updated = new SongCreateUpdateDTO();
        updated.setName("Updated Song");
        updated.setSeconds(200);

        HttpEntity<SongCreateUpdateDTO> request = new HttpEntity<>(updated, headers());
        restTemplate.put(baseUrl() + "/api/songs/" + songId, request);

        ResponseEntity<Song> response = restTemplate.getForEntity(baseUrl() + "/api/songs/" + songId, Song.class);
        assertNotNull(response.getBody());
        assertEquals("Updated Song", response.getBody().getName());
        assertEquals(200, response.getBody().getSeconds());
    }

    @Test
    @Order(10)
    void shouldDeleteSong() {
        restTemplate.delete(baseUrl() + "/api/songs/" + songId);

        ResponseEntity<Song> response = restTemplate.getForEntity(baseUrl() + "/api/songs/" + songId, Song.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(11)
    void shouldGetAllSongsFromAlbum() {
        ResponseEntity<Album[]> response = restTemplate.getForEntity(baseUrl() + "/api/albums", Album[].class);
        assertNotNull(response.getBody());
        Album a = response.getBody()[0];
        ResponseEntity<Song[]> response1 = restTemplate.getForEntity(baseUrl() + "/api/albums/" + a.getId().toString() + "/songs", Song[].class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertNotNull(response1.getBody());
        assertTrue(response1.getBody().length > 0);
    }
}

