package com.example.albumservice.client;


import com.example.albumservice.dto.SongDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class SongClient {

    private final RestTemplate restTemplate;
    private final String songServiceUrl;

    public SongClient(RestTemplate restTemplate,
                      @Value("${song.service.url:http://localhost:8082}") String songServiceUrl) {
        this.restTemplate = restTemplate;
        this.songServiceUrl = songServiceUrl;
    }

    public List<SongDTO> getSongsByAlbumId(UUID albumId) {
        try {
            String url = songServiceUrl + "/api/songs/album/" + albumId;
            ResponseEntity<List<SongDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SongDTO>>() {
                    }
            );
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Failed to fetch songs from song service: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void deleteSongsByAlbumId(UUID albumId) {
        try {
            String url = songServiceUrl + "/api/songs/album/" + albumId;
            restTemplate.delete(url);
            System.out.println("Deleted songs for album: " + albumId);
        } catch (Exception e) {
            System.err.println("Failed to delete songs from song service: " + e.getMessage());
            throw new RuntimeException("Failed to delete songs for album " + albumId, e);
        }
    }

}
