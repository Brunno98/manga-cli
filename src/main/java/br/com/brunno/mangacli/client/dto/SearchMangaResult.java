package br.com.brunno.mangacli.client.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SearchMangaResult(
        String result,
        List<FindedMangaData> data,
        int total
) {}