package br.com.brunno.mangacli.mangadex.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SearchMangaResult(
        String result,
        List<FindedMangaData> data,
        int total
) {
    public boolean isEmpty() {
        return data == null || data().isEmpty();
    }
}