package br.com.brunno.mangacli.command;

import br.com.brunno.mangacli.model.Manga;
import br.com.brunno.mangacli.repository.MangaRepository;
import br.com.brunno.mangacli.view.ListMangaView;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class List {

    private final MangaRepository mangaRepository;

    //TODO: to implement pagination on findAll
    @ShellMethod(key = "list", value = "List of your tracked mangas")
    public String listMangas() {
        java.util.List<Manga> all = mangaRepository.findAll();
        if (all.isEmpty()) return "No one manga added yet";

        return ListMangaView.build(all);
    }

}
