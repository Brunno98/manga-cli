package br.com.brunno.mangacli.command;

import br.com.brunno.mangacli.BaseShellTest;
import br.com.brunno.mangacli.client.MangadexClient;
import br.com.brunno.mangacli.repository.MangaRepository;
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

    @Test
    void givenANameWhenHelloWorldCommandShouldGreeting() {
        ShellTestClient.BaseShellSession<?> session = createSession("hello-world spring", false);

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Hello World spring");
        });
    }

}
