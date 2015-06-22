package com.kentchiu.spring.base.domain;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

public class DomainUtilTest {

    @Test
    public void testCopyNotNullProperties() throws Exception {
        TestBean source = new TestBean();
        source.setBigDecimal(BigDecimal.TEN);
        source.setIntegerProp(99);
        source.setStringProp("foo");
        source.setStringProp2("bar");

        TestBean target = new TestBean();
        target.setIntegerProp(88);
        target.setStringProp3("foobar");
        DomainUtil.copyNotNullProperties(source, target);

        assertThat(target.getBigDecimal(), is(BigDecimal.TEN));
        assertThat(target.getIntegerProp(), is(99));
        assertThat(target.getStringProp(), is("foo"));
        assertThat(target.getStringProp2(), is("bar"));
        assertThat(target.getStringProp3(), is("foobar"));
    }

    @Test
    public void testDateProperties_from_string_to_date() throws Exception {
        DateBean source = new DateBean();
        String dateStr = "2015-01-01 12:33:56";
        source.setDate(dateStr);

        TestBean target = new TestBean();
        DomainUtil.copyNotNullProperties(source, target);

        assertThat(target.getDate(), is(DateUtils.parseDate(dateStr,  "yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testDateProperties_from_data_to_date() throws Exception {
        String dateStr = "2015-01-01 12:33:56";
        TestBean source = new TestBean();
        source.setDate(DateUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss"));

        TestBean target = new TestBean();
        DomainUtil.copyNotNullProperties(source, target);

        assertThat(target.getDate(), is(DateUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testDateProperties_from_null_date_to_date_should_keeping_date_value() throws Exception {
        String dateStr = "2015-01-01 12:33:56";
        TestBean source = new TestBean();

        TestBean target = new TestBean();
        target.setDate(DateUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss"));
        DomainUtil.copyNotNullProperties(source, target);

        assertThat(target.getDate(), is(DateUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testDateProperties_from_empty_date_string_to_date_should_clean_date_value() throws Exception {
        DateBean source = new DateBean();
        source.setDate("");

        TestBean target = new TestBean();
        target.setDate(DateUtils.parseDate("2015-01-01 12:33:56", "yyyy-MM-dd HH:mm:ss"));
        DomainUtil.copyNotNullProperties(source, target);

        assertThat(target.getDate(), nullValue());
    }

    @Test
    public void testDateProperties_from_null_date_string_to_date_should_keeping_source_date_value() throws Exception {
        DateBean source = new DateBean();
        source.setDate(null);

        TestBean target = new TestBean();
        String dateStr = "2015-01-01 12:33:56";
        target.setDate(DateUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss"));
        DomainUtil.copyNotNullProperties(source, target);

        assertThat(target.getDate(), is(DateUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss")));
    }


    @Test
    public void testDateProperties_date_property_should_keep_without_same_property() throws Exception {
        String dateStr = "2015-01-01 12:33:56";
        Object source = new Object();

        TestBean target = new TestBean();
        target.setDate(DateUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss"));
        DomainUtil.copyNotNullProperties(source, target);

        assertThat(target.getDate(), is(DateUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testDateProperties_both_date_property_all_null() throws Exception {
        TestBean source = new TestBean();

        TestBean target = new TestBean();
        DomainUtil.copyNotNullProperties(source, target);

        assertThat(target.getDate(), nullValue());
    }


}