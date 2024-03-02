package br.com.brunno.mangacli.cli.view;

import br.com.brunno.mangacli.manga.Manga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.component.SingleItemSelector;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SelectMangaView extends AbstractShellComponent {

    public Optional<Manga> display(List<Manga> mangas) {
        List<SelectorItem<Manga>> items = mangas.stream()
                .map(manga -> SelectorItem.of(manga.getTitle(), manga)).collect(Collectors.toList());

        SingleItemSelector<Manga, SelectorItem<Manga>> component =
                new SingleItemSelector<>(getTerminal(), items, "Manga selection: ", null);

        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());

        SingleItemSelector.SingleItemSelectorContext<Manga, SelectorItem<Manga>> context =
                component.run(SingleItemSelector.SingleItemSelectorContext.empty());

        Optional<SelectorItem<Manga>> resultItem = context.getResultItem();

        if (resultItem.isEmpty()) {
            log.debug("ERROR: result item from Manga Selection is empty!");
            return Optional.empty();
        }

        return Optional.ofNullable(resultItem.get().getItem());
    }
}
