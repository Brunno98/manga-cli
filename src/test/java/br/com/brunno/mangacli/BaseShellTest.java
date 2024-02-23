package br.com.brunno.mangacli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.autoconfigure.ShellTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@ShellTest(terminalWidth = 120)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class BaseShellTest {

    @Autowired
    protected ShellTestClient client;

    protected void assertScreenContainsText(ShellTestClient.BaseShellSession<?> session, String text) {
        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen()).containsText(text);
        });
    }

    protected ShellTestClient.BaseShellSession<?> createSession(String command, boolean interactive) {
        if (interactive) {
            ShellTestClient.InteractiveShellSession session = client.interactive().run();
            session.write(session.writeSequence().command(command).build());
            return session;
        }
        else {
            String[] commands = command.split(" ");
            ShellTestClient.NonInteractiveShellSession session = client.nonInterative(commands).run();
            return session;
        }
    }

}
