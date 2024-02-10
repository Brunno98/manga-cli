package br.com.brunno.mangacli.view;

import br.com.brunno.mangacli.model.Manga;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;

import java.util.List;

import static java.lang.String.format;

public class ListMangaView {
    public static final String[] HEADER = new String[] {"id", "Title", "Chapters", "Status"};

    public static String build(List<Manga> mangas) {
        String[][] data = new String[mangas.size()+1][HEADER.length];
        data[0] = HEADER;

        for (int i = 0; i < mangas.size(); i++) {
            int row = i+1;
            Manga manga = mangas.get(i);
            data[row][0] = manga.getId();
            data[row][1] = manga.getTitle();
            data[row][2] = format("%d/%d", manga.getReaded(), manga.getTotalChapters());
            data[row][3] = manga.getReaded() == manga.getTotalChapters() ? "Completed" : "Reading";
        }

        ArrayTableModel tableModel = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(tableModel);
        tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.air);
        return tableBuilder.build().render(100);
    }
}
