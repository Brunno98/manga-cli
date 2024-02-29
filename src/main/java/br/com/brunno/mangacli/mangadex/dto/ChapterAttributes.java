package br.com.brunno.mangacli.mangadex.dto;

public record ChapterAttributes(
        String chapter,
        String title,
        String externalUrl,
        String createdAt,
        String updatedAt
) {
}
