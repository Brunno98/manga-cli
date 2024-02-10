package br.com.brunno.mangacli.client.dto;

import java.util.List;

public record MangaChaptersDto(
        String result,
        List<ChaptersData> data,
        int total
) {
}
