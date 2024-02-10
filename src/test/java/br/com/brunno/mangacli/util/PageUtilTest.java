package br.com.brunno.mangacli.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PageUtilTest {

    @Test
    void givenTotalItensAndItensPerPageShouldReturnTotalPages() {
        int totalItens = 10;
        int itensPerPage = 2;

        int result = PageUtil.totalPages(totalItens, itensPerPage);

        int expected = 5;

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void whenTotalItensIsNotAMultipleOfItensPerPageThenTotalPaagesShouldBeRoundedUp() {
        int totalItens = 10;
        int itensPerPage = 3;

        int result = PageUtil.totalPages(totalItens, itensPerPage);

        int expected = 4;

        Assertions.assertThat(result).isEqualTo(expected);

    }

    @Test
    void whenItensPerPageIs0ThenMaxPageShouldBe0() {
        int totalItens = 10;
        int itensPerPage = 0;

        int result = PageUtil.totalPages(totalItens, itensPerPage);

        int expected = 0;

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void whenTotalItensIs0ThenMaxPageShouldBe0() {
        int totalItens = 0;
        int itensPerPage = 5;

        int result = PageUtil.totalPages(totalItens, itensPerPage);

        int expected = 0;

        Assertions.assertThat(result).isEqualTo(expected);
    }

}
