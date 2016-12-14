package com.yay.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.util.Date;

/**
 * 描述: XXX
 * @version 1.0
 * @since 2016/12/14 18:05
 */
public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        return parseDate(source);
    }


    Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return DateUtils.parseDate(str.toString(),
                    "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}


