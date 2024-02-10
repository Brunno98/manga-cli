package br.com.brunno.mangacli.command;

import br.com.brunno.mangacli.client.MangadexClient;
import br.com.brunno.mangacli.client.dto.MangaChaptersDto;
import br.com.brunno.mangacli.client.dto.MangaDataDto;
import br.com.brunno.mangacli.client.dto.MangaDto;
import br.com.brunno.mangacli.model.Manga;
import br.com.brunno.mangacli.repository.MangaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import static java.lang.String.format;

@RequiredArgsConstructor
@ShellComponent
public class Add {

    private final MangadexClient mangadexClient;
    private final MangaRepository mangaRepository;

    @ShellMethod(key = "add", value = "Add manga to your tracking list")
    public String addManga(
            @ShellOption String mangaId
    ) {
        MangaDto mangaDto = mangadexClient.getMangaById(mangaId);
        if (!"ok".equalsIgnoreCase(mangaDto.result()) || mangaDto.data() == null) return "Manga " + mangaId + " not found";
        MangaChaptersDto mangaChaptersDto = mangadexClient
                .listChapters(mangaId, 1, "desc", "en", 0);

        MangaDataDto mangaData = mangaDto.data();
        String id = mangaData.id();
        String title = mangaData.attributes().title().en();
        String description = mangaData.attributes().description().en();
        int totalChapters = mangaChaptersDto.data().stream()
                .findFirst()
                .map(chapter -> Integer.parseInt(chapter.attributes().chapter()))
                .orElse(0);

        Manga manga = new Manga(id, title, description, totalChapters, 0);

        mangaRepository.save(manga);

        return format("Manga %s added to your list successfully", manga.getTitle());
    }
}
