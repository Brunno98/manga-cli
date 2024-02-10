package br.com.brunno.mangacli.util;

public class PageUtil {

    public static int totalPages(int totalItens, int itensPerPage) {
        if (itensPerPage == 0) return 0;
        return (int) Math.ceil((double) totalItens / itensPerPage);
    }
}
