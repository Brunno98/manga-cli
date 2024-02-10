package br.com.brunno.mangacli.command;

import br.com.brunno.mangacli.client.MangadexClient;
import br.com.brunno.mangacli.client.dto.FindedMangaData;
import br.com.brunno.mangacli.client.dto.SearchMangaResult;
import br.com.brunno.mangacli.model.Manga;
import br.com.brunno.mangacli.util.PageUtil;
import br.com.brunno.mangacli.view.SearchMangaView;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class Search {
    public static final int LIMIT_ROWS_RETURNED = 5;
    public static final int FIRST_PAGE = 1;

    private final MangadexClient mangadexClient;

    @ShellMethod(key = "search", value = "Search manga by title in 'https://mangadex.org'")
    public String searchManga(
            @ShellOption @NotBlank String title,
            @ShellOption(defaultValue = "1", help = "page number to view") @Min(FIRST_PAGE) int page
    ) {
        int offset = (page - FIRST_PAGE) * LIMIT_ROWS_RETURNED;
        SearchMangaResult result = mangadexClient.searchManga(title, LIMIT_ROWS_RETURNED, offset);

        List<FindedMangaData> data = result.data();
        if (data.isEmpty()) return page == FIRST_PAGE ?
                "Not found manga with title " + title
                : "No result in page " + page;


        List<Manga> mangas = data.stream().map(manga -> {
            String id = manga.id();
            String mangaTitle = manga.attributes().title().en();
            String description = manga.attributes().description().en();
            return new Manga(id, mangaTitle, description, 0, 0);
        }).collect(Collectors.toList());

        int maxPage = PageUtil.totalPages(result.total(), LIMIT_ROWS_RETURNED);

        return SearchMangaView.build(mangas, page, maxPage);
    }
}
