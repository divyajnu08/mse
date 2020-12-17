package com.virtue.csr.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;


@WritingConverter
public class MongoLocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
    @Override
    public Date convert(LocalDateTime date) {
        return date == null ? null : Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }
}
