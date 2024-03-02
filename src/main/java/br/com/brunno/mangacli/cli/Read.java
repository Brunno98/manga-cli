package br.com.brunno.mangacli.cli;

import br.com.brunno.mangacli.cli.view.SelectMangaView;
import br.com.brunno.mangacli.manga.Manga;
import br.com.brunno.mangacli.manga.MangaRepository;
import br.com.brunno.mangacli.mangadex.MangadexClient;
import br.com.brunno.mangacli.mangadex.dto.ChaptersData;
import br.com.brunno.mangacli.mangadex.dto.MangaChaptersDto;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@ShellComponent
public class Read {

    private final SelectMangaView selectMangaView;
    private final MangaRepository mangaRepository;
    private final Terminal terminal;
    private final MangadexClient mangadexClient;

    @ShellMethod(key = "read", value = "Update your manga's chapters readed")
    public void readManga(
            String title,
            @ShellOption(defaultValue = "1", help = "quantity of chapters readed") @Min(1) int quantity
    ) {
        List<Manga> mangasByTitle = mangaRepository.findByTitleContainingIgnoreCase(title);
        if (mangasByTitle.isEmpty()) {
            write("Manga not found");
            return;
        }

        Manga manga;
        if (mangasByTitle.size() == 1) {
            log.debug("Encontrado exatamente 1 manga com o titulo {}", title);
            manga = mangasByTitle.get(0);
        } else {
            log.debug("Encontrado {} mangas pelo titulo {}", mangasByTitle.size(), title);

            Optional<Manga> optionalManga = selectMangaView.display(mangasByTitle);

            if (optionalManga.isEmpty()) {
                log.debug("ERROR: result item from Manga Selection is empty!");
                write("Error!");
                return;
            }

            manga = optionalManga.get();
        }

        String mangaId = manga.getId();
        MangaChaptersDto mangaChaptersDto = mangadexClient.listChapters(mangaId, quantity + 1, "asc", "en", manga.getReaded());
        if (mangaChaptersDto == null || mangaChaptersDto.data() == null || mangaChaptersDto.data().size() == 0) {
            log.debug("Error: no chapters found for manga {}", manga.getTitle());
            write("Error: no chapters found for manga " + manga.getTitle());
            return;
        }
        List<ChaptersData> chapters = mangaChaptersDto.data();
        ChaptersData nextChapterToRead = chapters.get(chapters.size() - 1);
        ChaptersData lastChapterReaded = chapters.get(chapters.size() - 2);

        manga.setNextChapterToReadId(nextChapterToRead.id());
        manga.setLastReadedChapterNumber(lastChapterReaded.attributes().chapter());
        manga.readChapters(quantity);
        mangaRepository.save(manga);
    }

    private void write(String text) {
        terminal.writer().write(text);
        terminal.writer().flush();
    }
}
