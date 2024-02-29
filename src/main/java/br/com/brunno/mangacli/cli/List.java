package br.com.brunno.mangacli.cli;

import br.com.brunno.mangacli.manga.Manga;
import br.com.brunno.mangacli.manga.MangaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class List {

    private final MangaRepository mangaRepository;

    //TODO: to implement pagination on findAll
    @ShellMethod(key = "list", value = "List of your tracked mangas")
    public void listMangas() {
        java.util.List<Manga> all = mangaRepository.findAll();
//        if (all.isEmpty()) return "No one manga added yet";

//        return ListMangaView.build(all);
    }

}
