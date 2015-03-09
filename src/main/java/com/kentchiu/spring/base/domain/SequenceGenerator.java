package com.kentchiu.spring.base.domain;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class SequenceGenerator {

    public static String dateAndSequenceNumber(LocalDate date, int sequenceNo, int length, String delimiter) {
        DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
        formatterBuilder.appendPattern("yyMMdd");
        DateTimeFormatter formatter = formatterBuilder.toFormatter();
        return date.format(formatter) + delimiter + StringUtils.leftPad(Integer.toString(sequenceNo), length, '0');
    }

    public static String nextSeq(@Nullable String seq) {
        int length = 6;
        if (StringUtils.isBlank(seq)) {
            return dateAndSequenceNumber(LocalDate.now(), 1, length, "");
        } else {
            String datePart = StringUtils.substring(seq, 0, length);
            String numPart = StringUtils.substring(seq, length, 100);
            int i = Integer.parseInt(numPart);
            i++;
            String nextSeq = StringUtils.leftPad(Integer.toString(i), length, '0');
            return datePart + nextSeq;
        }
    }

}
