package br.com.brunno.mangacli.cli;

import br.com.brunno.mangacli.manga.Manga;
import br.com.brunno.mangacli.manga.MangaRepository;
import lombok.RequiredArgsConstructor;
import org.jline.terminal.Terminal;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@RequiredArgsConstructor
@ShellComponent
public class ListCommand {

    private final MangaRepository mangaRepository;
    private final Terminal terminal;

    //TODO: to implement pagination on findAll
    @ShellMethod(key = "list", value = "List of your tracked mangas")
    public void listMangas() {
        List<Manga> all = mangaRepository.findAll();
        if (all.isEmpty()) {
            write("No one manga added yet");
            return;
        }

        write("List of Mangas:");
        all.forEach(manga -> write(mangaListItem(manga)));
    }

    private static String mangaListItem(Manga manga) {
        return manga.getTitle() +
                "\t" +
                "Chapter " +
                manga.getReaded() +
                "/" +
                manga.getTotalChapters() +
                "\t" +
                "Last Chapter readed: " + manga.getLastReadedChapterNumber() +
                "\t" +
                manga.nextChapterLocation();
    }

    private void write(String text) {
        terminal.writer().println(text);
        terminal.writer().flush();
    }

}
