package testHelpers;

import br.com.brunno.mangacli.mangadex.dto.FindedMangaData;
import br.com.brunno.mangacli.mangadex.dto.LanguageOption;
import br.com.brunno.mangacli.mangadex.dto.MangaAttributes;
import br.com.brunno.mangacli.mangadex.dto.SearchMangaResult;

import java.util.ArrayList;
import java.util.List;

public class SearchMangaResultBuilder {

    private String result = "OK";
    private List<FindedMangaData> mangas = new ArrayList<>();


    private SearchMangaResultBuilder() {}

    public static SearchMangaResultBuilder builder() {
        return new SearchMangaResultBuilder();
    }

    public SearchMangaResultBuilder withResult(String result) {
        this.result = result;
        return this;
    }

    public SearchMangaResultBuilder withFindedManga(String id, String title, String description) {
        FindedMangaData findedMangaData = new FindedMangaData(
                id,
                new MangaAttributes(
                        new LanguageOption(title),
                        new LanguageOption(description)
                )
        );
        this.mangas.add(findedMangaData);
        return this;
    }

    public SearchMangaResult build() {
        return new SearchMangaResult(this.result, this.mangas, this.mangas.size());
    }

    public SearchMangaResultBuilder withoutManga() {
        this.mangas = new ArrayList<>();
        return this;
    }
}
