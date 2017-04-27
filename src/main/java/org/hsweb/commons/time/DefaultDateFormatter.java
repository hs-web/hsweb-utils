package org.hsweb.commons.time;

import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class DefaultDateFormatter implements DateFormatter {
    DateTimeFormatter formatter;

    Predicate<String> predicate;

    public DefaultDateFormatter(Pattern formatPattern, DateTimeFormatter formatter) {
        this(str -> formatPattern.matcher(str).matches(), formatter);
    }

    public DefaultDateFormatter(Predicate<String> predicate, DateTimeFormatter formatter) {
        this.predicate = predicate;
        this.formatter = formatter;
    }

    @Override
    public boolean support(String str) {
        return predicate.test(str);
    }

    @Override
    public Date format(String str) {
        return formatter.parseDateTime(str).toDate();
    }
}