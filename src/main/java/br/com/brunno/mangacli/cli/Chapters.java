package br.com.brunno.mangacli.cli;

import br.com.brunno.mangacli.manga.Chapter;
import br.com.brunno.mangacli.mangadex.MangadexClient;
import br.com.brunno.mangacli.mangadex.dto.MangaChaptersDto;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class Chapters {
    public static final int LIMIT_LIST = 10;
    public static final String ORDER = "desc";
    public static final String DEFAULT_LANGUAGE = "en"; // English
    public static final int FIRST_PAGE = 1;

    private final MangadexClient mangadexClient;

    @ShellMethod(key = "chapters", value = "List of manga's chapters available in 'https://mangadex.org'")
    public void listChapters(
            @ShellOption String mangaId,
            @ShellOption(defaultValue = "1", help = "page number to view") @Min(FIRST_PAGE) int page
    ) {
//        int offset = (page - FIRST_PAGE) * LIMIT_LIST;
//        MangaChaptersDto mangaChaptersDto = mangadexClient.listChapters(mangaId, LIMIT_LIST, ORDER, DEFAULT_LANGUAGE, offset);
//
//        List<Chapter> chapters = mangaChaptersDto.data().stream().map(chapter -> {
//            String id = chapter.id();
//            String title = chapter.attributes().title();
//            String chapterNumber = chapter.attributes().chapter();
//            String externalUrl = chapter.attributes().externalUrl();
//            String createdAt = chapter.attributes().createdAt();
//            String updatedAt = chapter.attributes().updatedAt();
//            return new Chapter(id, title, chapterNumber, externalUrl, createdAt, updatedAt);
//        }).collect(Collectors.toList());

//        int maxPage = PageUtil.totalPages(mangaChaptersDto.total(), LIMIT_LIST);
//
//        return ListChaptersView.build(chapters, page, maxPage);
    }
}
