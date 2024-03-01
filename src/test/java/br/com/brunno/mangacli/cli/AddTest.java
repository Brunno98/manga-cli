package br.com.brunno.mangacli.cli;

import br.com.brunno.mangacli.BaseShellTest;
import br.com.brunno.mangacli.manga.MangaRepository;
import br.com.brunno.mangacli.mangadex.MangadexClient;
import br.com.brunno.mangacli.mangadex.dto.SearchMangaResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.test.ShellTestClient;
import testHelpers.SearchMangaResultBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

public class AddTest extends BaseShellTest {
    public static final String COMMAND = "add";

    @MockBean
    MangadexClient mangadexClient;
    @MockBean
    MangaRepository mangaRepository;

    @Test
    void whenAddNewMangaFindManyThenShouldListOptionsOfMangasAndSaveTheSelected() {
        SearchMangaResult searchMangaResult = SearchMangaResultBuilder.builder()
                .withResult("OK")
                .withFindedManga("1", "someManga", "some description")
                .withFindedManga("2", "someManga 2", "another description")
                .build();
        Mockito.doReturn(searchMangaResult).when(mangadexClient).searchManga(any(), anyInt(), anyInt());

        ShellTestClient.BaseShellSession<?> session = createSession(COMMAND + " someManga", true);

        assertScreenContainsText(session, "someManga");
        assertScreenContainsText(session, "someManga 2");

        session.write(session.writeSequence().keyDown().carriageReturn().build());

        assertScreenContainsText(session, "Manga added!");
    }

    @Test
    void whenAddNewMangaFindOneMangaThenShouldSaveManga() {
        SearchMangaResult searchMangaResult = SearchMangaResultBuilder.builder()
                .withResult("OK")
                .withFindedManga("1", "someManga", "some description")
                .build();
        Mockito.doReturn(searchMangaResult).when(mangadexClient).searchManga(any(), anyInt(), anyInt());

        ShellTestClient.BaseShellSession<?> session = createSession(COMMAND + " someManga", true);

        assertScreenContainsText(session, "Manga added!");
    }

    @Test
    void whenNotFoundMangaShouldOutputNotFound() {
        Mockito.doReturn(SearchMangaResultBuilder.builder().withResult("OK").withoutManga().build())
                .when(mangadexClient).searchManga(any(), anyInt(), anyInt());

        ShellTestClient.BaseShellSession<?> session = createSession(COMMAND + " someManga", true);

        assertScreenContainsText(session, "Not found.");
    }
}
