package com.qurasense.gateway;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

    public static DateFormatter ISO_DATE_FORMATTER;
    static {
        ISO_DATE_FORMATTER = new DateFormatter();
        ISO_DATE_FORMATTER.setIso(DateTimeFormat.ISO.DATE_TIME);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(Date.class, new Formatter<Date>() {

            @Override
            public String print(Date object, Locale locale) {
                return ISO_DATE_FORMATTER.print(object, locale);
            }

            @Override
            public Date parse(String text, Locale locale) throws ParseException {
                return ISO_DATE_FORMATTER.parse(text, locale);
            }
        });
    }

}