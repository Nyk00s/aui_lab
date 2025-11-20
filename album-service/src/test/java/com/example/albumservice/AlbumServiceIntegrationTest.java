package com.example.albumservice;

import com.example.albumservice.client.SongClient;
import com.example.albumservice.dto.AlbumCreateUpdateDTO;
import com.example.albumservice.dto.AlbumReadDTO;
import com.example.albumservice.dto.SongDTO;
import com.example.albumservice.entity.Album;
import com.example.albumservice.repository.AlbumRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class AlbumServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AlbumRepository albumRepository;

    @MockBean
    private SongClient songClient;

    private static UUID testAlbumId;

    @BeforeEach
    void setUp() {
        albumRepository.deleteAll();
    }

    @Test
    @Order(1)
    void shouldCreateAlbum() throws Exception {
        AlbumCreateUpdateDTO dto = new AlbumCreateUpdateDTO(
                "Test Album",
                "Test Author",
                2025
        );

        MvcResult result = mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Album"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.yearOfRelease").value(2025))
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        AlbumReadDTO created = objectMapper.readValue(response, AlbumReadDTO.class);
        testAlbumId = UUID.fromString(created.getId());
    }

    @Test
    @Order(2)
    void shouldReturnConflictWhenCreatingDuplicateAlbum() throws Exception {
        // Create first album
        Album album = new Album.Builder()
                .name("Duplicate Album")
                .author("Author")
                .yearOfRelease(2025)
                .build();
        albumRepository.save(album);

        // Try to create album with same name
        AlbumCreateUpdateDTO dto = new AlbumCreateUpdateDTO(
                "Duplicate Album",  // Same name
                "Different Author",
                2026
        );

        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());  // Expect 409
    }

    @Test
    @Order(3)
    void shouldGetAlbumById() throws Exception {
        Album album = new Album.Builder()
                .name("Test Album")
                .author("Test Author")
                .yearOfRelease(2025)
                .build();
        album = albumRepository.save(album);

        mockMvc.perform(get("/api/albums/" + album.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Album"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.yearOfRelease").value(2025));
    }

    @Test
    @Order(4)
    void shouldReturnNotFoundForNonExistentAlbum() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(get("/api/albums/" + randomId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    void shouldGetAllAlbums() throws Exception {
        // Create test albums
        Album album1 = new Album.Builder()
                .name("Album 1")
                .author("Author 1")
                .yearOfRelease(2020)
                .build();
        Album album2 = new Album.Builder()
                .name("Album 2")
                .author("Author 2")
                .yearOfRelease(2021)
                .build();

        albumRepository.save(album1);
        albumRepository.save(album2);

        mockMvc.perform(get("/api/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists());
    }

    @Test
    @Order(6)
    void shouldUpdateAlbum() throws Exception {
        Album album = new Album.Builder()
                .name("Old Name")
                .author("Old Author")
                .yearOfRelease(2020)
                .build();
        album = albumRepository.save(album);

        AlbumCreateUpdateDTO updateDto = new AlbumCreateUpdateDTO(
                "Updated Album",
                "Test Author",
                2026
        );

        // Mock song client
        doNothing().when(songClient).updateSongs(any(UUID.class), anyString());

        mockMvc.perform(put("/api/albums/" + album.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Album"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.yearOfRelease").value(2026));

        // Verify song service was called to update songs
        verify(songClient, times(1)).updateSongs(album.getId(), "Updated Album");
    }

    @Test
    @Order(7)
    void shouldGetAlbumSongs() throws Exception {
        Album album = new Album.Builder()
                .name("Album with Songs")
                .author("Author")
                .yearOfRelease(2025)
                .build();
        album = albumRepository.save(album);

        // Mock song service response
        SongDTO song1 = new SongDTO(
                UUID.randomUUID().toString(),
                "Song 1",
                180,
                album.getId().toString(),
                "Album with Songs"
        );
        SongDTO song2 = new SongDTO(
                UUID.randomUUID().toString(),
                "Song 2",
                200,
                album.getId().toString(),
                "Album with Songs"
        );

        when(songClient.getSongsByAlbumId(album.getId()))
                .thenReturn(Arrays.asList(song1, song2));

        mockMvc.perform(get("/api/albums/" + album.getId() + "/songs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Song 1"))
                .andExpect(jsonPath("$[1].name").value("Song 2"));

        verify(songClient, times(1)).getSongsByAlbumId(album.getId());
    }

    @Test
    @Order(8)
    void shouldDeleteAlbum() throws Exception {
        Album album = new Album.Builder()
                .name("To Delete")
                .author("Author")
                .yearOfRelease(2025)
                .build();
        album = albumRepository.save(album);

        // Mock song client to not throw exception
        doNothing().when(songClient).deleteSongsByAlbumId(album.getId());

        mockMvc.perform(delete("/api/albums/" + album.getId()))
                .andExpect(status().isNoContent());

        // Verify songs were deleted first
        verify(songClient, times(1)).deleteSongsByAlbumId(album.getId());

        // Verify album deletion
        mockMvc.perform(get("/api/albums/" + album.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    void shouldDeleteAlbumAndCascadeDeleteSongs() throws Exception {
        // Create an album
        Album album = new Album.Builder()
                .name("Album with Songs to Delete")
                .author("Test Author")
                .yearOfRelease(2025)
                .build();
        album = albumRepository.save(album);

        // Mock song service to confirm songs exist
        SongDTO song1 = new SongDTO(
                UUID.randomUUID().toString(),
                "Song 1",
                180,
                album.getId().toString(),
                "Album with Songs to Delete"
        );

        when(songClient.getSongsByAlbumId(album.getId()))
                .thenReturn(Arrays.asList(song1));

        doNothing().when(songClient).deleteSongsByAlbumId(album.getId());

        // Delete the album
        mockMvc.perform(delete("/api/albums/" + album.getId()))
                .andExpect(status().isNoContent());

        // Verify that songClient.deleteSongsByAlbumId was called
        verify(songClient, times(1)).deleteSongsByAlbumId(album.getId());

        // Verify album is deleted
        mockMvc.perform(get("/api/albums/" + album.getId()))
                .andExpect(status().isNotFound());
    }
}