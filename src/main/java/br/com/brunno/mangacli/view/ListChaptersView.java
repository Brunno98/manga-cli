package br.com.brunno.mangacli.view;

import br.com.brunno.mangacli.model.Chapter;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;

import java.util.List;

public class ListChaptersView {
    public static final String[] HEADER = new String[] {"id", "title", "number", "location", "modified"};

    public static String build(List<Chapter> chapters, int page, int maxPage) {
        String main = buildMain(chapters);
        String footer = buildFooter(page, maxPage);
        return String.join("\n", main, footer);
    }

    private static String buildMain(List<Chapter> chapters) {
        String[][] data = new String[chapters.size()+1][HEADER.length];
        data[0] = HEADER;
        for (int i = 0; i < chapters.size(); i++) {
            Chapter chapter = chapters.get(i);
            data[i+1][0] = chapter.getId();
            data[i+1][1] = chapter.getTitle();
            data[i+1][2] = chapter.getNumber();
            data[i+1][3] = chapter.getLocation();
            data[i+1][4] = chapter.getLastModifiedDate();
        }

        ArrayTableModel tableModel = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(tableModel);
        tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.air);
        return tableBuilder.build().render(100);
    }

    private static String buildFooter(int page, int maxPage) {
        return "Page " + page + "/" + maxPage;
    }

}
