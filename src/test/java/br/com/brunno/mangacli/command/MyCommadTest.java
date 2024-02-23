package br.com.brunno.mangacli.command;

import br.com.brunno.mangacli.client.MangadexClient;
import br.com.brunno.mangacli.repository.MangaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.ShellTestClient.InteractiveShellSession;
import org.springframework.shell.test.autoconfigure.ShellTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@ShellTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MyCommadTest {

    @Autowired
    ShellTestClient client;

    @MockBean
    MangadexClient mangadexClient;

    @MockBean
    MangaRepository mangaRepository;

    @Test
    void givenANameWhenHelloWorldCommandShouldGreeting() {
        InteractiveShellSession session = client.interactive().run();

        session.write(session.writeSequence().text("hello-world spring").carriageReturn().build());
        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText("Hello World spring");
        });
    }

}
