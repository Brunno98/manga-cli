package br.com.brunno.mangacli.repository;

import br.com.brunno.mangacli.model.Manga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MangaRepository extends JpaRepository<Manga, String> {
}
