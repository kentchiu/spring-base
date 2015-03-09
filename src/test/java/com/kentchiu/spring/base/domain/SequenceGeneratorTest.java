package com.kentchiu.spring.base.domain;


import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SequenceGeneratorTest {

    @Test
    public void dateAndTenSequenceNumber() throws Exception {
        LocalDate date = LocalDate.of(2014, 3, 1);
        assertThat(SequenceGenerator.dateAndSequenceNumber(date, 1, 10, "-"), is("140301-0000000001"));
        assertThat(SequenceGenerator.dateAndSequenceNumber(date, 123, 10, "-"), is("140301-0000000123"));
        assertThat(SequenceGenerator.dateAndSequenceNumber(date.minusDays(1), 123, 10, "-"), is("140228-0000000123"));
    }


    @Test
    public void testNextQuoteSeq() throws Exception {
        assertThat(SequenceGenerator.nextSeq("140101000001"), is("140101000002"));
        assertThat(SequenceGenerator.nextSeq("140101000002"), is("140101000003"));
        assertThat(SequenceGenerator.nextSeq("140101000009"), is("140101000010"));
        assertThat(SequenceGenerator.nextSeq("140101000099"), is("140101000100"));
        assertThat(SequenceGenerator.nextSeq("140102000001"), is("140102000002"));
    }

    @Test
    public void testNextQuoteSeq_blank() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        assertThat(SequenceGenerator.nextSeq(null), is(datePart + "000001"));
        assertThat(SequenceGenerator.nextSeq(""), is(datePart + "000001"));
    }
}
