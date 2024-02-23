package br.com.brunno.mangacli.command;

import br.com.brunno.mangacli.BaseShellTest;
import br.com.brunno.mangacli.client.MangadexClient;
import br.com.brunno.mangacli.client.dto.SearchMangaResult;
import br.com.brunno.mangacli.repository.MangaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.test.ShellTestClient;
import testHelpers.SearchMangaResultBuilder;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

public class SearchTest extends BaseShellTest {

    @Autowired
    ShellTestClient client;

    @MockBean
    MangadexClient mangadexClient;

    @MockBean
    MangaRepository mangaRepository;

    @Test
    void givenATitleWhenSearchCommandShouldReturnOptionsOfManga() {
        SearchMangaResult searchMangaResult = SearchMangaResultBuilder.builder()
                .withResult("OK")
                .withFindedManga("1", "someManga", "some description")
                .withFindedManga("2", "someManga 2", "another description")
                .build();
        Mockito.doReturn(searchMangaResult).when(mangadexClient).searchManga(any(), anyInt(), anyInt());

        ShellTestClient.BaseShellSession<?> session = createSession("search someManga", true);

        assertScreenContainsText(session, "someManga");
        assertScreenContainsText(session, "someManga 2");
    }

    @Test
    void whenNotFoundMangaShouldOutputNotFound() {
        Mockito.doReturn(SearchMangaResultBuilder.builder().withResult("OK").withoutManga().build())
                .when(mangadexClient).searchManga(any(), anyInt(), anyInt());

        ShellTestClient.BaseShellSession<?> session = createSession("search Inexistent", true);

        assertScreenContainsText(session, "Not found manga");
    }
}
