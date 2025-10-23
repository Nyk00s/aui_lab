package com.example.demo.repository;

import java.util.UUID;
import java.util.List;
import com.example.demo.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, UUID> {
    List<Album> findByAuthorIgnoreCase(String author);
}
