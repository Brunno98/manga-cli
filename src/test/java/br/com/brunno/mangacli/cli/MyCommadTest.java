package br.com.brunno.mangacli.cli;

import br.com.brunno.mangacli.BaseShellTest;
import br.com.brunno.mangacli.cli.component.SelectMangaComponent;
import br.com.brunno.mangacli.mangadex.MangadexClient;
import br.com.brunno.mangacli.manga.MangaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellTestClient;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class MyCommadTest extends BaseShellTest {

    @Autowired
    ShellTestClient client;

    @MockBean
    MangadexClient mangadexClient;

    @MockBean
    MangaRepository mangaRepository;

    @MockBean
    SelectMangaComponent selectMangaComponent;

    @Test
    void givenANameWhenHelloWorldCommandShouldGreeting() {
        ShellTestClient.BaseShellSession<?> session = createSession("hello-world spring", false);

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Hello World spring");
        });
    }

}
