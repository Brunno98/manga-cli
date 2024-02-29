package br.com.brunno.mangacli.manga;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MangaRepository extends JpaRepository<Manga, String> {
}
