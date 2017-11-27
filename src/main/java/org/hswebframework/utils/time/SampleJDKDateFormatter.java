package org.hswebframework.utils.time;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SampleJDKDateFormatter implements DateFormatter {
    Predicate<String>          predicate;
    Supplier<SimpleDateFormat> formatSupplier;

    public SampleJDKDateFormatter(Predicate<String> predicate, Supplier<SimpleDateFormat> formatSupplier) {
        this.predicate = predicate;
        this.formatSupplier = formatSupplier;
    }

    @Override
    public boolean support(String str) {
        return predicate.test(str);
    }

    @Override
    public Date format(String str) {
        try {
            return formatSupplier.get().parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getPattern() {
        return formatSupplier.get().toPattern();
    }

    @Override
    public String toString(Date date) {
        return new DateTime(date).toString(getPattern());
    }
}