package br.com.brunno.mangacli.view;

import br.com.brunno.mangacli.model.Manga;
import br.com.brunno.mangacli.util.PageUtil;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;

import java.util.List;

import static java.lang.String.format;

public class SearchMangaView {
    public static final String[] HEADER = new String[] {"id", "title", "description"};

    public static String build(List<Manga> mangas, int page, int maxPage, int screenWidth) {
        String main = buildMain(mangas).render(screenWidth);
        String footer = buildFooter(page, maxPage);
        return String.join("\n", main, footer);
    }

    private static Table buildMain(List<Manga> mangas) {
        String[][] data = new String[mangas.size()+1][HEADER.length];
        data[0] = HEADER;
        for (int i = 0; i < mangas.size(); i++) {
            Manga manga = mangas.get(i);
            data[i+1][0] = manga.getId();
            data[i+1][1] = manga.getTitle();
            String description = manga.getDescription();
            if (description != null && description.length() >= 80) {
                description = description.substring(0, 77) + "...";
            }
            data[i+1][2] = description;
        }

        ArrayTableModel tableModel = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(tableModel);
        tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.air);
        return tableBuilder.build();
    }

    private static String buildFooter(int page, int maxPage) {
        StringBuilder footerBuilder = new StringBuilder();
//        if (page > 1) footerBuilder.append("PREV").append("\t");
        footerBuilder.append(format("page %d/%d", page, maxPage));
//        if (page < maxPage) footerBuilder.append("\t").append("NEXT");
        return footerBuilder.toString();
    }
}
