package br.com.brunno.mangacli.manga;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Manga {
    @Id
    private String id;
    private String title;
    transient private String description;
    private int totalChapters;
    private int readed;

    public void readChapters(int quantity) {
        int total = readed + quantity;
        if (total > totalChapters) {
            total = totalChapters;
        }
        readed = total;
    }
}
