package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;

@Configuration
public class CassandraConfig {
    @Bean
    public CassandraCustomConversions cassandraCustomConversions(CurrencyWriteConverter currencyWriteConverter,
                                                                 CurrencyReadConverter currencyReadConverter,
                                                                 LocalWriteConverter localWriteConverter,
                                                                 LocaleReadConverter localeReadConverter) {
        return new CassandraCustomConversions(Arrays.asList(currencyWriteConverter, currencyReadConverter, localWriteConverter, localeReadConverter));
    }

    @Component
    @WritingConverter
    public static class CurrencyWriteConverter implements Converter<Currency, String> {

        @Override
        public String convert(Currency currency) {
            return currency.toString();
        }
    }

    @Component
    @ReadingConverter
    public static class CurrencyReadConverter implements Converter<String, Currency> {

        @Override
        public Currency convert(String s) {
            return Currency.getInstance(s);
        }
    }

    @Component
    @WritingConverter
    public static class LocalWriteConverter implements Converter<Locale, String> {

        @Override
        public String convert(Locale locale) {
            return locale.toString();
        }
    }

    @Component
    @ReadingConverter
    public static class LocaleReadConverter implements Converter<String, Locale> {

        @Override
        public Locale convert(String s) {
            return StringUtils.parseLocaleString(s);
        }
    }
}
