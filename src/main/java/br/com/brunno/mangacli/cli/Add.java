package br.com.brunno.mangacli.cli;

import br.com.brunno.mangacli.manga.Manga;
import br.com.brunno.mangacli.manga.MangaRepository;
import br.com.brunno.mangacli.mangadex.MangadexClient;
import br.com.brunno.mangacli.mangadex.dto.FindedMangaData;
import br.com.brunno.mangacli.mangadex.dto.MangaChaptersDto;
import br.com.brunno.mangacli.mangadex.dto.SearchMangaResult;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.springframework.shell.component.SingleItemSelector;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@ShellComponent
public class Add extends AbstractShellComponent {
    public static final int MAX_ROWS_RETURNED = 5;

    private final MangaRepository mangaRepository;
    private final MangadexClient mangadexClient;
    private final Terminal terminal;

    @ShellMethod(key = "add", value = "add manga searching in 'https://mangadex.org' by title")
    public void addManga(@ShellOption @NotBlank String title) {
        log.debug("Buscando manga por titulo {}", title);
        SearchMangaResult result = mangadexClient.searchManga(title, MAX_ROWS_RETURNED, 0);

        if (result == null || result.isEmpty()) {
            log.debug("Not found manga with title {}", title);
            write("Not found.");
            return;
        }

        List<Manga> mangas = toMangaList(result);

        Manga manga;
        if (mangas.size() > 1) {
            Optional<Manga> optionalManga = displaySingleSelectionManga(title, mangas);
            if (optionalManga.isEmpty()) {
                log.debug("ERROR: item from Manga Selection is null!");
                write("Error!");
                return;
            }
            manga = optionalManga.get();
        } else {
            log.debug("Encontrado exatamente 1 manga pelo titulo {}", title);
            manga = mangas.get(0);
        }
        log.debug("Manga selecionado: title {} - id: {}", manga.getTitle(), manga.getId() );

        MangaChaptersDto mangaChaptersDto = mangadexClient.listChapters(manga.getId(), 0, "desc", "en", 0);
        if (mangaChaptersDto != null) {
            manga.setTotalChapters(mangaChaptersDto.total());
        }

        mangaRepository.save(manga);
        log.debug("Manga {} salvo com sucesso", title);
        write("Manga added!");
    }

    private Optional<Manga> displaySingleSelectionManga(String title, List<Manga> mangas) {
        log.debug("Encontrado {} mangas pelo titulo {}", mangas.size(), title);

        List<SelectorItem<Manga>> items = mangas.stream()
                .map(manga -> SelectorItem.of(manga.getTitle(), manga)).collect(Collectors.toList());

        SingleItemSelector<Manga, SelectorItem<Manga>> component =
                new SingleItemSelector<>(getTerminal(), items, "Manga selection: ", null);

        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());

        SingleItemSelector.SingleItemSelectorContext<Manga, SelectorItem<Manga>> context =
                component.run(SingleItemSelector.SingleItemSelectorContext.empty());

        Optional<SelectorItem<Manga>> resultItem = context.getResultItem();

        if (resultItem.isEmpty()) {
            log.debug("ERROR: result item from Manga Selection is empty!");
            write("Error!");
            return Optional.empty();
        }

        return Optional.ofNullable(resultItem.get().getItem());
    }

    private static List<Manga> toMangaList(SearchMangaResult result) {
        List<FindedMangaData> data = result.data();
        return data.stream().map(manga -> {
            String id = manga.id();
            String mangaTitle = manga.attributes().title().en();
            String description = manga.attributes().description().en();
            return new Manga(id, mangaTitle, description, 0);
        }).collect(Collectors.toList());
    }

    private void write(String text) {
        terminal.writer().println(text);
        terminal.writer().flush();
    }
}
