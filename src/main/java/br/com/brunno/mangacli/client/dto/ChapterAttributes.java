package br.com.brunno.mangacli.client.dto;

public record ChapterAttributes(
        String chapter,
        String title,
        String externalUrl,
        String createdAt,
        String updatedAt
) {
}
