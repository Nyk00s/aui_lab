package com.example.songservice.client;

import com.example.songservice.dto.AlbumDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

@Component
public class AlbumClient {

    private final RestTemplate restTemplate;
    private final String albumServiceUrl;

    public AlbumClient(RestTemplate restTemplate,
                       @Value("${album.service.url:http://localhost:8081}") String albumServiceUrl) {
        this.restTemplate = restTemplate;
        this.albumServiceUrl = albumServiceUrl;
    }

    public AlbumDTO getAlbumById(UUID albumId) {
        try {
            String url = albumServiceUrl + "/api/albums/" + albumId;
            return restTemplate.getForObject(url, AlbumDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch album from album service", e);
        }
    }

    public List<AlbumDTO> getAllAlbums() {
        try {
            String url = albumServiceUrl + "/api/albums";
            ResponseEntity<List<AlbumDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<AlbumDTO>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch albums from album service", e);
        }
    }
}