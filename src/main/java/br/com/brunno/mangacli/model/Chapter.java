package br.com.brunno.mangacli.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Chapter {
    public static final String BASE_URL = "https://mangadex.org/chapter/%s";

    private String id;
    private String title;
    private String number;
    @Getter(AccessLevel.PRIVATE)
    private String externalUrl;
    private String createdAt;
    private String updatedAt;

    public String getLocation() {
        if (isNotEmpty(externalUrl)) return externalUrl;

        return String.format(BASE_URL, id);
    }

    public String getLastModifiedDate() {
        if (isNotEmpty(updatedAt)) return updatedAt;

        return createdAt;
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.isBlank();
    }

}
