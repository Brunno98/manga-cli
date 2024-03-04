package br.com.brunno.mangacli.cli.component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Page {
    private final int itensPerPage;
    private final int totalItens;

    private int offset;

    public int limit() {
        return Math.min(offset + itensPerPage, totalItens);
    }

    public int offset() {
        return offset;
    }

    public void next() {
        if (offset + itensPerPage >= totalItens) return;
        offset += itensPerPage;
    }

    public void previus() {
        offset = Math.max(offset - itensPerPage, 0);
    }

    public boolean hasOnlyOnePage() {
        return itensPerPage >= totalItens;
    }

    public int itensInPage() {
        return limit() - offset;
    }
}
