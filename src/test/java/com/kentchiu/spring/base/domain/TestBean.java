package com.kentchiu.spring.base.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class TestBean {

    private Integer integerProp;
    private String stringProp;
    private String stringProp2;

    public String getStringProp3() {
        return stringProp3;
    }

    public void setStringProp3(String stringProp3) {
        this.stringProp3 = stringProp3;
    }

    public String getStringProp2() {
        return stringProp2;
    }

    public void setStringProp2(String stringProp2) {
        this.stringProp2 = stringProp2;
    }

    private String stringProp3;
    private BigDecimal bigDecimal;
    private Date date;
    private LocalDate localDate;
    private LocalTime localTime;
    private LocalDateTime localDateTime;

    public Boolean getBooleanProp() {
        return booleanProp;
    }

    public void setBooleanProp(Boolean booleanProp) {
        this.booleanProp = booleanProp;
    }

    private Boolean booleanProp;
    private SubBean subBean;

    public Integer getIntegerProp() {
        return integerProp;
    }

    public void setIntegerProp(Integer integerProp) {
        this.integerProp = integerProp;
    }

    public String getStringProp() {
        return stringProp;
    }

    public void setStringProp(String stringProp) {
        this.stringProp = stringProp;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public SubBean getSubBean() {
        return subBean;
    }

    public void setSubBean(SubBean subBean) {
        this.subBean = subBean;
    }


}


class SubBean {
    private Integer integerProp2;
    private String stringProp2;
    private BigDecimal bigDecimal2;
    private Date date2;
    private LocalDate localDate2;
    private LocalTime localTime2;
    private LocalDateTime localDateTime2;

    public Integer getIntegerProp2() {
        return integerProp2;
    }

    public void setIntegerProp2(Integer integerProp2) {
        this.integerProp2 = integerProp2;
    }

    public String getStringProp2() {
        return stringProp2;
    }

    public void setStringProp2(String stringProp2) {
        this.stringProp2 = stringProp2;
    }

    public BigDecimal getBigDecimal2() {
        return bigDecimal2;
    }

    public void setBigDecimal2(BigDecimal bigDecimal2) {
        this.bigDecimal2 = bigDecimal2;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public LocalDate getLocalDate2() {
        return localDate2;
    }

    public void setLocalDate2(LocalDate localDate2) {
        this.localDate2 = localDate2;
    }

    public LocalTime getLocalTime2() {
        return localTime2;
    }

    public void setLocalTime2(LocalTime localTime2) {
        this.localTime2 = localTime2;
    }

    public LocalDateTime getLocalDateTime2() {
        return localDateTime2;
    }

    public void setLocalDateTime2(LocalDateTime localDateTime2) {
        this.localDateTime2 = localDateTime2;
    }


}


class DateBean {

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}