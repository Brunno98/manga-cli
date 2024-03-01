package br.com.brunno.mangacli.manga;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Manga {
    @Id
    private String id;
    private String title;
    transient private String description;
    @Setter
    private int totalChapters;
    private int readed;
    private String lastReadedChapterId;

    public Manga(String id, String title, String description, int totalChapters) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.totalChapters = totalChapters;
    }

    public void readChapters(int quantity) {
        int total = readed + quantity;
        if (total > totalChapters) {
            total = totalChapters;
        }
        readed = total;
    }

    public String nextChapterLocation() {
        if (lastReadedChapterId == null) return "Not found";
        Chapter lastChapter = new Chapter(lastReadedChapterId, null, null, null, null, null);
        return lastChapter.getLocation();
    }

}
