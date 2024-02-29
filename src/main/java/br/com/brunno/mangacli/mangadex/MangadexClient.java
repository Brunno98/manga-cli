package br.com.brunno.mangacli.mangadex;

import br.com.brunno.mangacli.mangadex.dto.MangaChaptersDto;
import br.com.brunno.mangacli.mangadex.dto.MangaDto;
import br.com.brunno.mangacli.mangadex.dto.SearchMangaResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "mangadex", url = "https://api.mangadex.org")
public interface MangadexClient {

    @GetMapping("/manga")
    SearchMangaResult searchManga(
            @RequestParam String title,
            @RequestParam int limit,
            @RequestParam int offset
    );

    @GetMapping("/manga/{mangaId}/feed")
    MangaChaptersDto listChapters(
            @PathVariable String mangaId,
            @RequestParam int limit,
            @RequestParam(value = "order[chapter]") String order,
            @RequestParam(value = "translatedLanguage[]") String language,
            @RequestParam(value = "offset") int offset
    );

    @GetMapping("/manga/{mangaId}")
    MangaDto getMangaById(@PathVariable String mangaId);

}
