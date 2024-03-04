package br.com.brunno.mangacli.cli.component;

import br.com.brunno.mangacli.manga.Manga;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.springframework.shell.component.SingleItemSelector;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static br.com.brunno.mangacli.cli.component.ListMangaComponent.Action.*;

@Slf4j
@Component
public class ListMangaComponent extends AbstractShellComponent {
    public static final int ITENS_PER_PAGE = 5;

    public void run(List<Manga> mangas) {
        String[][] data = new String[mangas.size()][4];
        for (int i = 0; i < mangas.size(); i++) {
            Manga manga = mangas.get(i);
            data[i][0] = manga.getTitle();
            data[i][1] = "Chapter " + manga.getReaded() + "/" + manga.getTotalChapters();
            data[i][2] = "Last Chapter readed: " + manga.getLastReadedChapterNumber();
            data[i][3] = manga.nextChapterLocation();
        }

        Action action;
        Page page = new Page(ITENS_PER_PAGE, mangas.size());
        do {
            String[][] subData = new String[page.itensInPage()][4];
            for (int index = page.offset(), subIndex = 0; index < page.limit(); index++, subIndex++) {
                subData[subIndex][0] = data[index][0];
                subData[subIndex][1] = data[index][1];
                subData[subIndex][2] = data[index][2];
                subData[subIndex][3] = data[index][3];
            }

            TableBuilder tableBuilder = getTableBuilder(subData);

            write(tableBuilder.build().render(getTerminal().getWidth()));

            if (page.hasOnlyOnePage()) break;

            action = getActionSelectedFromUser();
            if (NEXT.equals(action)) {
                page.next();
            } else if (PREVIOUS.equals(action)) {
                page.previus();
            }
        } while (!EXIT.equals(action));
    }

    private TableBuilder getTableBuilder(String[][] data) {
        ArrayTableModel tableModel = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(tableModel);
        tableBuilder.addFullBorder(BorderStyle.fancy_light);
        return tableBuilder;
    }

    private Action getActionSelectedFromUser() {
        List<SelectorItem<Action>> selectorItems = List.of(
                SelectorItem.of("Next", NEXT),
                SelectorItem.of("Previous", PREVIOUS),
                SelectorItem.of("Exit", EXIT)
        );
        SingleItemSelector<Action, SelectorItem<Action>> actionSelector =
                new SingleItemSelector<>(getTerminal(), selectorItems, "Action", null);
        actionSelector.setResourceLoader(getResourceLoader());
        actionSelector.setTemplateExecutor(getTemplateExecutor());

        SingleItemSelector.SingleItemSelectorContext<Action, SelectorItem<Action>> context =
                actionSelector.run(SingleItemSelector.SingleItemSelectorContext.empty());

        Optional<SelectorItem<Action>> optionalResultItem = context.getResultItem();
        if (optionalResultItem.isEmpty()) {
            log.debug("Error: result item from context is empty!");
            log.debug("Setting 'EXIT' as action because an error ocurried");
            return EXIT;
        }
        return optionalResultItem.get().getItem();
    }

    private void write(String text) {
        Terminal terminal = getTerminal();
        terminal.writer().println(text);
        terminal.writer().flush();
    }

    enum Action {
        NEXT, PREVIOUS, EXIT
    }
}
