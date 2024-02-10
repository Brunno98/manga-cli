package br.com.brunno.mangacli.command;

import br.com.brunno.mangacli.client.MangadexClient;
import br.com.brunno.mangacli.client.dto.FindedMangaData;
import br.com.brunno.mangacli.client.dto.LanguageOption;
import br.com.brunno.mangacli.client.dto.MangaAttributes;
import br.com.brunno.mangacli.client.dto.SearchMangaResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.ShellTestClient.InteractiveShellSession;
import org.springframework.shell.test.autoconfigure.ShellTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ShellTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SearchTest {

    @Autowired
    ShellTestClient client;

    @MockBean
    MangadexClient mangadexClient;

    @Test
    void givenATitleWhenSearchCommandShouldReturnOptionsOfManga() {
        Mockito.doReturn(buildSearchMangaResult()).when(mangadexClient).searchManga(any(), anyInt(), anyInt());

        InteractiveShellSession session = client.interactive().run();

        session.write(session
                    .writeSequence()
                    .text("search someManga")
                    .carriageReturn()
                    .build()
        );

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> ShellAssertions.assertThat(session.screen())
                .containsText("someManga").containsText("someManga 2"));
    }

    @Test
    void whenNotFoundMangaShouldOutputNotFound() {
        Mockito.doReturn(SearchMangaResult.builder().result("ok").data(List.of()).build())
                .when(mangadexClient).searchManga(any(), anyInt(), anyInt());

        InteractiveShellSession session = client.interactive().run();

        session.write(session
                .writeSequence()
                .text("search Inexistent")
                .carriageReturn()
                .build()
        );

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> ShellAssertions.assertThat(session.screen())
                .containsText("Not found manga"));
    }

    private SearchMangaResult buildSearchMangaResult() {
        return new SearchMangaResult(
                "ok", List.of(
                new FindedMangaData(
                        "1",
                        new MangaAttributes(
                                new LanguageOption("someManga"),
                                new LanguageOption("some description")
                        )
                ),
                new FindedMangaData(
                        "2",
                        new MangaAttributes(
                                new LanguageOption("someManga 2"),
                                new LanguageOption("another description")
                        )
                )
            ),
        2
        );
    }
}
