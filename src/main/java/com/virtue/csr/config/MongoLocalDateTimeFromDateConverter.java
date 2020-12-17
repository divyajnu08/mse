package com.virtue.csr.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;


@ReadingConverter
public class MongoLocalDateTimeFromDateConverter implements Converter<Date, LocalDateTime> {
    @Override
    public LocalDateTime convert(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
