package com.example.albumservice;


import com.example.albumservice.entity.Album;
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
}
