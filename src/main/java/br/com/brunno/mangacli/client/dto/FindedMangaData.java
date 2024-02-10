package br.com.brunno.mangacli.client.dto;

public record FindedMangaData(
        String id,
        MangaAttributes attributes
) {}