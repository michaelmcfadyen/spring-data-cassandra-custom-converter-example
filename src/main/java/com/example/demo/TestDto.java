package com.example.demo;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Currency;
import java.util.Locale;

@Table("test")
public class TestDto {

    @PrimaryKey
    private String pk;
    @Column("locale")
    private Locale locale;
    @Column("currency")
    private Currency currency;

    public TestDto(String pk, Locale locale, Currency currency) {
        this.pk = pk;
        this.locale = locale;
        this.currency = currency;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
