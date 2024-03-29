package br.com.brunno.mangacli.command;

import br.com.brunno.mangacli.model.Manga;
import br.com.brunno.mangacli.repository.MangaRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Optional;

@RequiredArgsConstructor
@ShellComponent
public class Read {

    private final MangaRepository mangaRepository;

    @ShellMethod(key = "read", value = "Update your manga's chapters readed")
    public String readManga(
            String mangaId,
            @ShellOption(defaultValue = "1", help = "quantity of chapters readed") @Min(1) int quantity
    ) {
        Optional<Manga> searchById = mangaRepository.findById(mangaId);
        if (searchById.isEmpty()) return "Manga not found";
        Manga manga = searchById.get();

        manga.readChapters(quantity);

        mangaRepository.save(manga);

        return new List(mangaRepository).listMangas();
    }
}
