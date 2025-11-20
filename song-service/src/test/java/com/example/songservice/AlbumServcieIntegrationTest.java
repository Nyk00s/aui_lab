package com.example.songservice;

import com.example.songservice.client.AlbumClient;
import com.example.songservice.dto.AlbumDTO;
import com.example.songservice.dto.SongCreateUpdateDTO;
import com.example.songservice.dto.SongReadDTO;
import com.example.songservice.entity.AlbumSimplified;
import com.example.songservice.entity.Song;
import com.example.songservice.repository.SongRepository;
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
class SongServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SongRepository songRepository;

    @MockBean
    private AlbumClient albumClient;

    private static UUID testAlbumId = UUID.randomUUID();
    private static UUID testSongId;

    @BeforeEach
    void setUp() {
        songRepository.deleteAll();

        // Mock album service response
        AlbumDTO mockAlbum = new AlbumDTO();
        mockAlbum.setId(testAlbumId.toString());
        mockAlbum.setName("Test Album");
        mockAlbum.setAuthor("Test Author");
        mockAlbum.setYearOfRelease(2025);

        when(albumClient.getAlbumById(testAlbumId)).thenReturn(mockAlbum);
    }

    @Test
    @Order(1)
    void shouldCreateSong() throws Exception {
        SongCreateUpdateDTO dto = new SongCreateUpdateDTO();
        dto.setName("Test Song");
        dto.setSeconds(180);
        dto.setAlbumId(testAlbumId);

        MvcResult result = mockMvc.perform(post("/api/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Song"))
                .andExpect(jsonPath("$.seconds").value(180))
                .andExpect(jsonPath("$.albumName").value("Test Album"))
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        SongReadDTO created = objectMapper.readValue(response, SongReadDTO.class);
        testSongId = UUID.fromString(created.getId());

        verify(albumClient, times(1)).getAlbumById(testAlbumId);
    }

    @Test
    @Order(2)
    void shouldReturnBadRequestWhenAlbumDoesNotExist() throws Exception {
        UUID nonExistentAlbumId = UUID.randomUUID();

        when(albumClient.getAlbumById(nonExistentAlbumId)).thenReturn(null);

        SongCreateUpdateDTO dto = new SongCreateUpdateDTO();
        dto.setName("Song for Non-existent Album");
        dto.setSeconds(180);
        dto.setAlbumId(nonExistentAlbumId);

        mockMvc.perform(post("/api/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    void shouldReturnBadRequestWhenSecondsIsNegative() throws Exception {
        SongCreateUpdateDTO dto = new SongCreateUpdateDTO();
        dto.setName("Invalid Song");
        dto.setSeconds(-10);  // Negative seconds
        dto.setAlbumId(testAlbumId);

        mockMvc.perform(post("/api/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    void shouldReturnConflictWhenSongNameEqualsAlbumName() throws Exception {
        SongCreateUpdateDTO dto = new SongCreateUpdateDTO();
        dto.setName("Test Album");  // Same as album name
        dto.setSeconds(180);
        dto.setAlbumId(testAlbumId);

        mockMvc.perform(post("/api/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(5)
    void shouldGetSongById() throws Exception {
        Song song = new Song.Builder()
                .name("Test Song")
                .seconds(180)
                .album(new AlbumSimplified(testAlbumId, "Test Album"))
                .build();
        song = songRepository.save(song);

        mockMvc.perform(get("/api/songs/" + song.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Song"))
                .andExpect(jsonPath("$.seconds").value(180))
                .andExpect(jsonPath("$.albumName").value("Test Album"));
    }

    @Test
    @Order(6)
    void shouldGetAllSongs() throws Exception {
        Song song1 = new Song.Builder()
                .name("Song 1")
                .seconds(180)
                .album(new AlbumSimplified(testAlbumId, "Test Album"))
                .build();
        Song song2 = new Song.Builder()
                .name("Song 2")
                .seconds(200)
                .album(new AlbumSimplified(testAlbumId, "Test Album"))
                .build();

        songRepository.save(song1);
        songRepository.save(song2);

        mockMvc.perform(get("/api/songs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists());
    }

    @Test
    @Order(7)
    void shouldGetSongsByAlbumId() throws Exception {
        UUID albumId = UUID.randomUUID();

        Song song1 = new Song.Builder()
                .name("Song 1")
                .seconds(180)
                .album(new AlbumSimplified(albumId, "Album 1"))
                .build();
        Song song2 = new Song.Builder()
                .name("Song 2")
                .seconds(200)
                .album(new AlbumSimplified(albumId, "Album 1"))
                .build();
        Song song3 = new Song.Builder()
                .name("Song 3")
                .seconds(150)
                .album(new AlbumSimplified(UUID.randomUUID(), "Album 2"))
                .build();

        songRepository.save(song1);
        songRepository.save(song2);
        songRepository.save(song3);

        mockMvc.perform(get("/api/songs/album/" + albumId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].albumId").value(albumId.toString()))
                .andExpect(jsonPath("$[1].albumId").value(albumId.toString()));
    }

    @Test
    @Order(8)
    void shouldUpdateSong() throws Exception {
        Song song = new Song.Builder()
                .name("Old Song Name")
                .seconds(180)
                .album(new AlbumSimplified(testAlbumId, "Test Album"))
                .build();
        song = songRepository.save(song);

        SongCreateUpdateDTO updateDto = new SongCreateUpdateDTO();
        updateDto.setName("Updated Song");
        updateDto.setSeconds(200);
        updateDto.setAlbumId(testAlbumId);

        mockMvc.perform(put("/api/songs/" + song.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Song"))
                .andExpect(jsonPath("$.seconds").value(200));
    }

    @Test
    @Order(9)
    void shouldDeleteSong() throws Exception {
        Song song = new Song.Builder()
                .name("To Delete")
                .seconds(180)
                .album(new AlbumSimplified(testAlbumId, "Test Album"))
                .build();
        song = songRepository.save(song);

        mockMvc.perform(delete("/api/songs/" + song.getId()))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/songs/" + song.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(10)
    void shouldDeleteSongsByAlbumId() throws Exception {
        UUID albumId = UUID.randomUUID();

        Song song1 = new Song.Builder()
                .name("Song 1")
                .seconds(180)
                .album(new AlbumSimplified(albumId, "Test Album"))
                .build();
        Song song2 = new Song.Builder()
                .name("Song 2")
                .seconds(200)
                .album(new AlbumSimplified(albumId, "Test Album"))
                .build();

        songRepository.save(song1);
        songRepository.save(song2);

        // Delete all songs for this album
        mockMvc.perform(delete("/api/songs/album/" + albumId))
                .andExpect(status().isNoContent());

        // Verify all songs are deleted
        mockMvc.perform(get("/api/songs/album/" + albumId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(11)
    void shouldUpdateSongsWhenAlbumNameChanges() throws Exception {
        UUID albumId = UUID.randomUUID();

        Song song1 = new Song.Builder()
                .name("Song 1")
                .seconds(180)
                .album(new AlbumSimplified(albumId, "Old Album Name"))
                .build();
        Song song2 = new Song.Builder()
                .name("Song 2")
                .seconds(200)
                .album(new AlbumSimplified(albumId, "Old Album Name"))
                .build();

        songRepository.save(song1);
        songRepository.save(song2);

        // Update album name in songs
        mockMvc.perform(put("/api/songs/album/" + albumId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"New Album Name\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].albumName").value("New Album Name"))
                .andExpect(jsonPath("$[1].albumName").value("New Album Name"));
    }
}