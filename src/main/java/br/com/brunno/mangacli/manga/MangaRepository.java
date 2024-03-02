package br.com.brunno.mangacli.manga;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MangaRepository extends JpaRepository<Manga, String> {
    List<Manga> findByTitleContainingIgnoreCase(String title);
}
