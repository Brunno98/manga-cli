package br.com.brunno.mangacli.cli;

import br.com.brunno.mangacli.manga.Manga;
import br.com.brunno.mangacli.manga.MangaRepository;
import br.com.brunno.mangacli.mangadex.MangadexClient;
import br.com.brunno.mangacli.mangadex.dto.FindedMangaData;
import br.com.brunno.mangacli.mangadex.dto.SearchMangaResult;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.springframework.shell.component.flow.ComponentFlow;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@ShellComponent
public class Add {
    public static final int MAX_ROWS_RETURNED = 5;

    private final ComponentFlow.Builder flowBuilder;
    private final MangaRepository mangaRepository;
    private final MangadexClient mangadexClient;
    private final Terminal terminal;

    @ShellMethod(key = "add", value = "add manga searching in 'https://mangadex.org' by title")
    public void addManga(@ShellOption @NotBlank String title) {
        log.debug("Buscando manga por titulo {}", title);
        SearchMangaResult result = mangadexClient.searchManga(title, MAX_ROWS_RETURNED, 0);

        List<Manga> mangas = toMangaList(result);

        if (mangas.size() > 1) {
            log.debug("Encontrado exatamente {} mangas pelo titulo {}", mangas.size(), title);
            HashMap<String, String> singleSelectItems = new HashMap<>();
            mangas.forEach(manga -> singleSelectItems.put(manga.getTitle(), manga.getTitle()));
            ComponentFlow flow = flowBuilder.reset().clone()
                    .withSingleItemSelector("MangaSelection")
                    .selectItems(singleSelectItems)
                    .and()
                    .build();
            log.debug("Rodando flow de seleção de manga");
            Object selectionFromFlow = flow.run().getContext().get("MangaSelection");
            if (selectionFromFlow == null) {
                log.debug("Flow não returnou resultado");
                return;
            }
            String mangaSelection = selectionFromFlow.toString();
            Optional<Manga> optionalManga = mangas.stream().filter(manga -> mangaSelection.equals(manga.getTitle())).findFirst();
            if (optionalManga.isEmpty()) {
                log.debug("Resultado do flow de seleçao ({}) não encontrado na lista de mangas", mangaSelection);
                return;
            }
            mangaRepository.save(optionalManga.get());
            log.debug("Manga salvo com sucesso");
            return;
        }

        if (mangas.size() == 1) {
            log.debug("Encontrado exatamente 1 manga pelo titulo {}", title);
            Manga manga = mangas.get(0);
            mangaRepository.save(manga);
            write("Manga added!");
            log.debug("Manga {} salvo com sucesso sem necessidade do usuario escolher", title);
            return;
        }
        write("Not found.");
    }

    private static List<Manga> toMangaList(SearchMangaResult result) {
        List<FindedMangaData> data = result.data();
        return data.stream().map(manga -> {
            String id = manga.id();
            String mangaTitle = manga.attributes().title().en();
            String description = manga.attributes().description().en();
            return new Manga(id, mangaTitle, description, 0, 0);
        }).collect(Collectors.toList());
    }

    private void write(String text) {
        terminal.writer().println(text);
        terminal.writer().flush();
    }
}